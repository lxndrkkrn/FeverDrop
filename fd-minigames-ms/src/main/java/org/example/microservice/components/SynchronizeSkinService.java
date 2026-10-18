package org.example.microservice.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.services.skincard.SkinCardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor

public class SynchronizeSkinService {

    private final SkinCardService skinCardService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(propagation = Propagation.MANDATORY)
    public void playerWinSkin() {

    }

}
