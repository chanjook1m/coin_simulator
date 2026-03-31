package com.coin.simulator.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderMarketBuyRequest(
        @NotBlank(message = "symbol은 필수 값입니다.")
        String symbol,

        @NotNull(message = "quantity는 필수 값입니다.")
        @Positive(message = "quantity는 0보다 커야 합니다.")
        BigDecimal quantity
) {
}
