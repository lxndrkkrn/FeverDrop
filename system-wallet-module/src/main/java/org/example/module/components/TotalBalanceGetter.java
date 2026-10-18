package org.example.module.components;

import dto.WalletBalanceUpdateDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.module.dtos.WalletResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor

public class TotalBalanceGetter {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private RestClient restClient;

    @Value("${market.api.base-url:https://aim.market}")
    private String baseUrl;

    @Value("${market.api.key}")
    private String apiKey;

    @Value("${prizePool.multiply}")
    private BigDecimal prizePoolMultiply;

    @PostConstruct
    public void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(15000);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .requestFactory(requestFactory)
                .build();
    }

    @Scheduled(fixedDelay = 300000)
    public void getterHandler() {
        log.info("Начинаем парсинг баланса проекта с API Aim.Market...");

        try {

            WalletResponseDto responseDto = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.aim.market")
                            .path("/v1/api/partner/account")
                            .queryParam("apiKey", apiKey)
                            .build())
                    .retrieve()
                    .body(WalletResponseDto.class);

            if (responseDto != null && responseDto.balance() != null && responseDto.currency() != null) {
                log.info("Баланс SystemWallet успешно получен: {} {}, Time: {}", responseDto.balance(), responseDto.currency(), LocalDateTime.now());

                BigDecimal prizePool = responseDto.balance().multiply(prizePoolMultiply);

                WalletBalanceUpdateDto dto = new WalletBalanceUpdateDto(responseDto.balance(), prizePool, responseDto.currency());

                kafkaTemplate.send("wallet-update-topic", dto);

                log.info("Обновленная информация о балансе улетела в брокер");

            } else {
                log.warn("Пришёл невалидный (возможно пустой) ответ: {} {}, Time: {}", responseDto.balance(), responseDto.currency(), LocalDateTime.now());
            }

        } catch (RestClientResponseException e) {

            log.error("Aim.Market вернул ошибку HTTP. Статус-код: {}, Тело ответа: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Ошибка при парсинга баланса проекта с Aim.Market", e);
        }

    }

}
