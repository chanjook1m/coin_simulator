package com.coin.simulator.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient upbitRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.upbit.com")
                .build();
    }
}
