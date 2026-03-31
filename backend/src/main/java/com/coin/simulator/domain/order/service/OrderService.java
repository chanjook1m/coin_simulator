package com.coin.simulator.domain.order.service;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.exception.CoinNotFoundException;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.domain.order.dto.OrderLimitBuyRequest;
import com.coin.simulator.domain.order.dto.OrderMarketBuyRequest;
import com.coin.simulator.domain.order.dto.OrderResponse;
import com.coin.simulator.domain.order.entity.Order;
import com.coin.simulator.domain.order.repository.OrderRepository;
import com.coin.simulator.domain.price.dto.PriceResponse;
import com.coin.simulator.domain.price.service.PriceService;
import com.coin.simulator.domain.user.entity.User;
import com.coin.simulator.domain.user.exception.UserNotFoundException;
import com.coin.simulator.domain.user.repository.UserRepository;
import com.coin.simulator.domain.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final PriceService priceService;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    @Transactional
    public OrderResponse placeMarketBuyOrder(@Valid @RequestBody OrderMarketBuyRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        // 시세 조회
        PriceResponse price = priceService.getLatestPrice(symbol);
        BigDecimal executedPrice = price.price();
        BigDecimal quantity = request.quantity();
        BigDecimal totalAmount = executedPrice.multiply(quantity);

        // 잔액 확인
        walletService.checkEnoughBalance(userId, totalAmount);

        // 주문 생성
        Order order = Order.marketBuy(user, coin, executedPrice, quantity);
        orderRepository.save(order);

        // 잔액 차감
        walletService.debit(userId, totalAmount);

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse placeLimitBuyOrder(@Valid @RequestBody OrderLimitBuyRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        // 시세 조회
        PriceResponse price = priceService.getLatestPrice(symbol);
        BigDecimal currentPrice = price.price();
        BigDecimal quantity = request.quantity();
        BigDecimal limitPrice = request.limitPrice();
        BigDecimal maxAmount = limitPrice.multiply(quantity);

        // 잔액 확인
        walletService.checkEnoughBalance(userId, maxAmount);

        // 주문 생성
        Order order = Order.limitBuyPending(user, coin, limitPrice, quantity);
        orderRepository.save(order);

        // 현재가 <= 지정가면 즉시 체결
        if (currentPrice == null || currentPrice.compareTo(limitPrice) <= 0) {
            order.fillAll(currentPrice);

            BigDecimal executedAmount = currentPrice.multiply(quantity);
            walletService.debit(userId, executedAmount);
        }
        // else PENDING 상태 유지

        return OrderResponse.from(order);
    }
}
