package com.coin.simulator.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsistencyMetricService {

    private final MeterRegistry meterRegistry;

    private Counter walletInconsistencyCounter;
    private Counter holdingInconsistencyCounter;

    @PostConstruct
    void init() {
        walletInconsistencyCounter = Counter.builder("wallet_inconsistency_total")
                .description("Wallet.balance != Ledger 현금 합계인 경우")
                .tag("service", "coin-simulator")
                .register(meterRegistry);

        holdingInconsistencyCounter = Counter.builder("holding_inconsistency_total")
                .description("Holding 수량 != 기준 데이터(체결/원장)인 경우")
                .tag("service", "coin-simulator")
                .register(meterRegistry);
    }

    public void incWalletInconsistency() {
        walletInconsistencyCounter.increment();
    }

    public void incHoldingInconsistency() {
        holdingInconsistencyCounter.increment();
    }
}