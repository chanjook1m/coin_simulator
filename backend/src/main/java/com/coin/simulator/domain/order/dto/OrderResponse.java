package com.coin.simulator.domain.order.dto;

import com.coin.simulator.domain.order.entity.Order;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(
        Long orderId,
        String symbol,
        String type,
        String method,
        String status,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal totalAmount, // 총 소요 금액 (price * quantity)
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        // 시장가 주문은 targetPrice가 없을 수 있으므로,
        // 실제 체결 로직에서 기록된 가격을 사용한다고 가정합니다.
        // (필요에 따라 order.getTargetPrice() 또는 별도의 executedPrice 필드 사용)
        BigDecimal price = order.getTargetPrice();
        BigDecimal quantity = order.getTotalQuantity();

        return OrderResponse.builder()
                .orderId(order.getId())
                .symbol(order.getCoin().getSymbol())
                .type(order.getType().name())
                .method(order.getMethod().name())
                .status(order.getStatus().name())
                .price(price)
                .quantity(quantity)
                .totalAmount(price.multiply(quantity))
                .createdAt(order.getCreatedAt())
                .build();
    }
}
