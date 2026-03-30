package com.coin.simulator.common.config;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.domain.price.entity.Price;
import com.coin.simulator.domain.price.repository.PriceRepository;
import com.coin.simulator.domain.user.entity.User;
import com.coin.simulator.domain.user.repository.UserRepository;
import com.coin.simulator.domain.wallet.entity.Wallet;
import com.coin.simulator.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;
    private final PriceRepository priceRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("DataInitializer: already initialized");
            return;
        }

        // 1) 유저 생성
        User user = User.builder()
                .username("testuser")
                .password("{noop}password") // 시큐리티 쓰면 인코딩 규칙 맞춰주기
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        // 2) 코인 생성 (BTC, KRW-BTC)
        Coin btc = Coin.builder()
                .symbol("BTC")
                .name("비트코인")
                .market("KRW-BTC")
                .build();
        coinRepository.save(btc);

        // 3) 지갑 생성 (유저 연관 + 1,000만 원)
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(new BigDecimal("10000000"))
                .build();
        walletRepository.save(wallet);

        // 4) 현재 시세 하나 넣기 (BTC 100만)
        Price price = Price.of(
                btc,
                new BigDecimal("1000000"),
                LocalDateTime.now()
        );
        priceRepository.save(price);

        log.info("DataInitializer: created userId={}, username={}, coinSymbol=BTC",
                user.getId(), user.getUsername());
    }
}
