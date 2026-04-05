package com.coin.simulator.domain.order.controller;

import com.coin.simulator.domain.order.dto.OrderLimitBuyRequest;
import com.coin.simulator.domain.order.dto.OrderLimitSellRequest;
import com.coin.simulator.domain.order.dto.OrderMarketBuyRequest;
import com.coin.simulator.domain.order.dto.OrderMarketSellRequest;
import com.coin.simulator.domain.order.dto.OrderResponse;
import com.coin.simulator.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/market/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeMarketBuy(@RequestBody OrderMarketBuyRequest request,
                                        @RequestHeader("X-USER-ID") Long userId) {
        return orderService.placeMarketBuyOrder(request, userId);
    }

    @PostMapping("/limit/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeLimitBuy(@RequestBody OrderLimitBuyRequest request,
                                       @RequestHeader("X-USER-ID") Long userId) {
        return orderService.placeLimitBuyOrder(request, userId);
    }

    @PostMapping("/market/sell")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeMarketSell(@RequestBody OrderMarketSellRequest request,
                                         @RequestHeader("X-USER-ID") Long userId) {
        return orderService.placeMarketSellOrder(request, userId);
    }

    @PostMapping("/limit/sell")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeLimitSell(@RequestBody OrderLimitSellRequest request,
                                        @RequestHeader("X-USER-ID") Long userId) {
        return orderService.placeLimitSellOrder(request, userId);
    }
}
