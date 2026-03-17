package com.coin.simulator.infrastructure.exchange;

import org.springframework.web.client.RestTemplate;

public class UpbitClient implements ExchangeClient {
    private final RestTemplate restTemplate = new RestTemplate();

}
