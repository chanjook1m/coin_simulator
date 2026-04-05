package com.coin.simulator.domain.wallet.service;

import com.coin.simulator.domain.ledger.entity.Ledger;
import com.coin.simulator.domain.ledger.repository.LedgerRepository;
import com.coin.simulator.domain.wallet.entity.Wallet;
import com.coin.simulator.domain.wallet.exception.InSufficientBalanceException;
import com.coin.simulator.domain.wallet.exception.WalletNotFoundException;
import com.coin.simulator.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final LedgerRepository ledgerRepository;

    @Transactional(readOnly = true)
    public void checkEnoughBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InSufficientBalanceException();
        }
    }


    @Transactional
    public void debit(Long userId, BigDecimal amount, String reason) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        wallet.debit(amount);

        Ledger ledger = Ledger.builder()
                .user(wallet.getUser())
                .asset("KRW")
                .changeAmount(amount)
                .reason(reason).build();

        ledgerRepository.save(ledger);
    }

    @Transactional
    public void credit(Long userId, BigDecimal amount, String reason) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        wallet.credit(amount);

        Ledger ledger = Ledger.builder()
                .user(wallet.getUser())
                .asset("KRW")
                .changeAmount(amount)
                .reason(reason).build();

        ledgerRepository.save(ledger);

    }
}
