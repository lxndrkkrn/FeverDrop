package org.example.microservice.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.services.domain.InventoryService;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class SkinListener {

    private final InventoryService inventoryService;


}
