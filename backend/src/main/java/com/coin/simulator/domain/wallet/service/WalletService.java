package com.coin.simulator.domain.wallet.service;

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

    @Transactional(readOnly = true)
    public void checkEnoughBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InSufficientBalanceException();
        }
    }


    @Transactional
    public void debit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        wallet.debit(amount);
    }

    @Transactional
    public void credit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(userId));

        wallet.credit(amount);
    }
}
