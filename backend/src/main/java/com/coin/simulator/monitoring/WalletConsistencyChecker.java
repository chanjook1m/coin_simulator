package com.coin.simulator.monitoring;

import com.coin.simulator.domain.ledger.repository.LedgerRepository;
import com.coin.simulator.domain.wallet.entity.Wallet;
import com.coin.simulator.domain.wallet.repository.WalletRepository;
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
public class WalletConsistencyChecker {

    private final ConsistencyMetricService metricsService;
    private final WalletRepository walletRepository;
    private final LedgerRepository ledgerRepository;

    @Scheduled(fixedDelayString = "60000") // 60초마다 검사
    @Transactional(readOnly = true)
    public void checkWalletConsistency() {
        List<Wallet> wallets = walletRepository.findAll();

        for (Wallet wallet : wallets) {
            Long userId = wallet.getUser().getId();

            BigDecimal ledgerCash = ledgerRepository.sumCashByUserId(userId);
            if (ledgerCash == null) ledgerCash = BigDecimal.ZERO;

            if (wallet.getBalance().compareTo(ledgerCash) != 0) {
                metricsService.incWalletInconsistency();
                log.error("[CONSISTENCY] Wallet mismatch userId={}, walletBalance={}, ledgerCash={}",
                        userId, wallet.getBalance(), ledgerCash);
            }
        }
    }
}