package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.services.domain.SkinService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class SkinAppService {

    private final SkinService skinService;



}
