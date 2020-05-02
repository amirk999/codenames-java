package com.klar.codenames.game.service;

import com.klar.codenames.game.model.Card;
import com.klar.codenames.game.repository.CardRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository repository;

    public CardService(CardRepository repository) {
        this.repository = repository;
    }

    public List<Card> findAll() {
        return repository.findAll();
    }

    public Card findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    public Card create(Card gameInstance) {
        return repository.save(gameInstance);
    }

    public Card save(Card card, Long id) throws Exception {
        return repository.findById(id).map(existingCard -> {
            existingCard.setIndex(card.getIndex());
            existingCard.setColor(card.getColor());
            existingCard.setStatus(card.getStatus());
            existingCard.setBody(card.getBody());
            return repository.save(card);
        }).orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    public Card deleteById(Long id) throws Exception {
        return repository.findById(id).map(existingCard -> {
            repository.deleteById(id);
            return existingCard;
        }).orElseThrow(() -> new Exception(String.valueOf(id)));
    }
}
