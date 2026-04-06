package com.coin.simulator.monitoring;

import com.coin.simulator.domain.holding.entity.Holding;
import com.coin.simulator.domain.holding.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldingConsistencyChecker {

    private final ConsistencyMetricService metricsService;
    private final HoldingRepository holdingRepository;

    @Scheduled(fixedDelayString = "60000")
    @Transactional(readOnly = true)
    public void checkHoldingConsistency() {
        List<Holding> holdings = holdingRepository.findAll();

        for (Holding holding : holdings) {
            boolean invalid = false;
            
            if (holding.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                invalid = true;
            }

            if (holding.getAvgPrice() == null
                    || holding.getAvgPrice().compareTo(BigDecimal.ZERO) < 0) {
                invalid = true;
            }

            if (invalid) {
                metricsService.incHoldingInconsistency();
                Long userId = holding.getWallet().getUser().getId();
                String symbol = holding.getCoin().getSymbol();
                log.error("[CONSISTENCY] Holding invalid userId={}, symbol={}, quantity={}, avgPrice={}",
                        userId, symbol, holding.getQuantity(), holding.getAvgPrice());
            }
        }
    }
}