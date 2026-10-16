package org.example.microservice.services.skincard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.repositories.SkinCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class SkinCardService {

    private final SkinCardRepository skinCardRepository;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinCard findById(Long id) {
        return skinCardRepository.findById(id)
                .orElseThrow(() -> new NotFound("Карточка скина по ID не найдена"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinCard findByFullName(String fullName) {
        return skinCardRepository.findByFullName(fullName)
                .orElseThrow(() -> new NotFound("Карточка скина по названию не найдена"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<SkinCard> findAllByFullName(List<String> fullNameList) {
        return skinCardRepository.findAllByFullNameIn(fullNameList);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SkinCard createSkinCard(String fullName, BigDecimal marketPrice, String imageUrl, String rarity) {
        log.info("Создание карточки скина {} ({}), price: {}, image url: {}", fullName, rarity, marketPrice, imageUrl);

        if (fullName == null || fullName.isEmpty()) {
            throw new BadRequest("При создании карточки скина, обязательный аргумент fullName не должен быть пустым");
        }

        SkinCard card = new SkinCard();

        card.setFullName(fullName);
        card.setMarketPrice(marketPrice);
        card.setImageUrl(imageUrl);
        card.setRarity(rarity);

        skinCardRepository.save(card);
        log.info("Успешно создана карточка скина {} ({}), price: {}, image url: {}", fullName, rarity, marketPrice, imageUrl);

        return card;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteSkinCard(Long id) {
        log.info("Удаление карточки скина, ID: {}", id);

        if (id == null) {
            throw new BadRequest("При создании карточки скина, обязательный аргумент id не должен быть пустым");
        }

        skinCardRepository.deleteById(id);

        log.info("Успешно удалена карточка, ID: {}", id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAll(List<SkinCard> list) {
        if (!list.isEmpty()) {
            skinCardRepository.saveAll(list);
        }
    }

}
