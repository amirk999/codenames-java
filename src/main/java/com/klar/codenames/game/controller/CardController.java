package com.klar.codenames.game.controller;

import com.klar.codenames.game.model.Card;
import com.klar.codenames.game.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {
    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    // TODO: may need to remove the below endpoints

    @GetMapping("/cards")
    public List<Card> getAllCards() {
        return service.findAll();
    }

    @PostMapping("/cards")
    public Card createCard(@RequestBody Card card) {
        return service.create(card);
    }

    @PutMapping("/cards/{id}")
    public Card updateCard(@RequestBody Card card, @PathVariable Long id) throws Exception {
        return service.save(card, id);
    }

}
