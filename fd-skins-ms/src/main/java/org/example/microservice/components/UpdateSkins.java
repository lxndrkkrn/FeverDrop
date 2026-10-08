package org.example.microservice.components;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.microservice.services.application.SkinAppService;
import org.example.microservice.services.domain.SkinService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j

public class UpdateSkins {

    private final SkinService skinService;
    private final SkinAppService skinAppService;

    @Value("${market.api.key}")
    private final String baseUrl;

    private final RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

    @Value("${market.api.key}")
    private final String apiKey;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void parseAndUpdateSkins() {

        log.info("Начинаем парсинг скинов с API Aim.Market...");



    }

}
