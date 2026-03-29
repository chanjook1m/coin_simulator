package com.coin.simulator.domain.order.controller;

import com.coin.simulator.domain.order.dto.OrderMarketBuyRequest;
import com.coin.simulator.domain.order.dto.OrderResponse;
import com.coin.simulator.domain.order.service.OrderService;
import com.coin.simulator.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
                                        @AuthenticationPrincipal User user) {
        return orderService.placeMarketBuyOrder(request, user.getId());
    }
}
