package org.example.microservice.services.casegame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.games.CaseGame;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.repositories.CaseGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class CaseGameService {

    private final CaseGameRepository caseGameRepository;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public CaseGame findCaseById(Long id) {
        return caseGameRepository.findById(id)
                .orElseThrow(() -> new NotFound("Кейс не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public CaseGame findCaseByName(String name) {
        return caseGameRepository.findByName(name)
                .orElseThrow(() -> new NotFound("Кейс по имени не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<CaseGame> findCaseByPrice(BigDecimal min, BigDecimal max) {
        return caseGameRepository.findByPriceBetween(min, max);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public CaseGame createCase(String name, String category, BigDecimal price, String imageUrl, List<SkinCard> skins) {
        log.info("Добавления кейса {} ({}), Price: {}", name, category, price);

        CaseGame case_ = new CaseGame();

        case_.setName(name);
        case_.setCategory(category);
        case_.setPrice(price);
        case_.setImageUrl(imageUrl);

        if (skins != null) {
            for (SkinCard skin : skins) {
                case_.addSkin(skin);
            }
        }

        caseGameRepository.save(case_);

        log.info("Новый кейс создан, ID: {}", case_.getId());

        return case_;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCase(Long id) {
        log.info("Удаление кейса с ID: {}", id);

        caseGameRepository.deleteById(id);

        log.info("Удалён кейс");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void changePrice(Long id, BigDecimal price) {
        log.info("Изменение цены на {} для кейса {}", price, id);

        CaseGame case_ = caseGameRepository.findById(id)
                .orElseThrow(() -> new NotFound("Кейс не найден"));

        case_.setPrice(price);

        log.info("Цена на кейс ({}) изменена на {}", id, price);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<SkinCard> addSkinsToCase(List<SkinCard> list, Long id) {
        log.info("Добавление скинов в кейс ({})", id);

        CaseGame case_ = caseGameRepository.findById(id)
                .orElseThrow(() -> new NotFound("Кейс не найден"));

        if (list != null) {
            for (SkinCard skin : list) {
                case_.addSkin(skin);
            }
        }

        caseGameRepository.save(case_);

        log.info("Скины в кейс ({}) добавленны", id);
        return case_.getSkins();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<SkinCard> takeSkinsFromCase(List<SkinCard> list, Long id) {
        log.info("Удаление скинов из кейса ({})", id);

        CaseGame case_ = caseGameRepository.findById(id)
                .orElseThrow(() -> new NotFound("Кейс не найден"));

        if (list != null) {
            for (SkinCard skin : list) {
                case_.removeSkin(skin);
            }
        }

        caseGameRepository.save(case_);

        log.info("Скины из кейса ({}) удалены", id);
        return case_.getSkins();
    }

}
