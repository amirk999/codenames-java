package com.klar.codenames.game.dto;

import com.klar.codenames.game.model.Card;
import com.klar.codenames.game.model.CardStatus;
import com.klar.codenames.game.model.Color;
import lombok.Data;

import java.util.Comparator;

@Data
public class CardDTO implements Comparable<CardDTO> {

    private Long id;
    private int index;
    private Color color;
    private String body;
    private CardStatus status;
    private Long gameId;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.index = card.getIndex();
        this.color = card.getColor();
        this.body = card.getBody();
        this.status = card.getStatus();
        this.gameId = card.getGame().getId();
    }

    @Override
    public int compareTo(CardDTO c) {
        return Integer.compare(this.getIndex(), c.getIndex());
    }
}
