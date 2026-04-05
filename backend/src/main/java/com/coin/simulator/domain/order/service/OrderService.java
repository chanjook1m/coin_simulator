package com.coin.simulator.domain.order.service;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.exception.CoinNotFoundException;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.domain.holding.service.HoldingService;
import com.coin.simulator.domain.order.dto.OrderLimitBuyRequest;
import com.coin.simulator.domain.order.dto.OrderLimitSellRequest;
import com.coin.simulator.domain.order.dto.OrderMarketBuyRequest;
import com.coin.simulator.domain.order.dto.OrderMarketSellRequest;
import com.coin.simulator.domain.order.dto.OrderResponse;
import com.coin.simulator.domain.order.entity.Order;
import com.coin.simulator.domain.order.repository.OrderRepository;
import com.coin.simulator.domain.price.dto.PriceResponse;
import com.coin.simulator.domain.price.service.PriceService;
import com.coin.simulator.domain.user.entity.User;
import com.coin.simulator.domain.user.exception.UserNotFoundException;
import com.coin.simulator.domain.user.repository.UserRepository;
import com.coin.simulator.domain.wallet.entity.Wallet;
import com.coin.simulator.domain.wallet.exception.WalletNotFoundException;
import com.coin.simulator.domain.wallet.repository.WalletRepository;
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
    private final HoldingService holdingService;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public OrderResponse placeMarketBuyOrder(@Valid @RequestBody OrderMarketBuyRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

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

        // 보유 코인 증가
        holdingService.increaseCoin(wallet, coin, quantity, executedPrice);

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse placeLimitBuyOrder(@Valid @RequestBody OrderLimitBuyRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

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
        if (currentPrice.compareTo(limitPrice) <= 0) {
            order.fillAll(currentPrice);

            BigDecimal executedAmount = currentPrice.multiply(quantity);
            walletService.debit(userId, executedAmount);

            // 보유 코인 증가
            holdingService.increaseCoin(wallet, coin, quantity, currentPrice);
        }
        // else PENDING 상태 유지

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse placeMarketSellOrder(@Valid @RequestBody OrderMarketSellRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        // 시세 조회
        PriceResponse price = priceService.getLatestPrice(symbol);
        BigDecimal executedPrice = price.price();
        BigDecimal quantity = request.quantity();
        BigDecimal totalAmount = executedPrice.multiply(quantity); // 매도 금액

        // 코인 보유 수량 확인
        holdingService.checkEnoughCoin(wallet, coin, quantity);

        // 주문 생성 (즉시 체결)
        Order order = Order.marketSell(user, coin, executedPrice, quantity);
        orderRepository.save(order);

        // 코인 수량 차감 + 현금 잔액 증가
        holdingService.decreaseCoin(wallet, coin, quantity);
        walletService.credit(userId, totalAmount);

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse placeLimitSellOrder(@Valid @RequestBody OrderLimitSellRequest request, Long userId) {
        String symbol = request.symbol().toUpperCase();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Coin coin = coinRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CoinNotFoundException(symbol));

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        // 시세 조회
        PriceResponse price = priceService.getLatestPrice(symbol);
        BigDecimal currentPrice = price.price();
        BigDecimal quantity = request.quantity();
        BigDecimal limitPrice = request.limitPrice();
        BigDecimal maxAmount = limitPrice.multiply(quantity); // 최대 매도 금액 (참고용)

        // 보유 수량 확인 (대기 주문까지 포함해서 수량 잠그는 정책이면 여기서 체크)
        holdingService.checkEnoughCoin(wallet, coin, quantity);

        // 주문 생성 (PENDING)
        Order order = Order.limitSellPending(user, coin, limitPrice, quantity);
        orderRepository.save(order);

        // 현재가 >= 지정가면 즉시 체결
        if (currentPrice != null && currentPrice.compareTo(limitPrice) >= 0) {
            order.fillAll(currentPrice);

            BigDecimal executedAmount = currentPrice.multiply(quantity);

            holdingService.decreaseCoin(wallet, coin, quantity);
            walletService.credit(userId, executedAmount);
        }
        // else PENDING 상태 유지

        return OrderResponse.from(order);
    }
}
