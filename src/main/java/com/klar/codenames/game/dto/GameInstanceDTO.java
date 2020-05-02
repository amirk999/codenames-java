package com.klar.codenames.game.dto;

import com.klar.codenames.game.model.Card;
import com.klar.codenames.game.model.Color;
import com.klar.codenames.game.model.GameInstance;
import com.klar.codenames.game.model.GameStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class GameInstanceDTO {
    private Long id;
    private String name;
    private GameStatus status;
    private int redRemaining;
    private int blueRemaining;
    private Color currentTurn;
    private List<CardDTO> cards;

    public GameInstanceDTO(GameInstance game) {
        this.id = game.getId();
        this.name = game.getName();
        this.status = game.getStatus();
        this.redRemaining = game.getRedRemaining();
        this.blueRemaining = game.getBlueRemaining();
        this.currentTurn = game.getCurrentTurn();

        List<CardDTO> cards = new ArrayList<CardDTO>();
        for(Card card : game.getCards()) {
            cards.add(new CardDTO(card));
        }

        Collections.sort(cards);

        this.cards = cards;
    }
}
