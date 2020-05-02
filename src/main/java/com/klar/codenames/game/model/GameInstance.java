package com.klar.codenames.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="games")
public class GameInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Card> cards;

    @Enumerated
    @Column(columnDefinition = "int2")
    private GameStatus status;

    @Column(name = "red_remaining", columnDefinition = "int2")
    private int redRemaining;

    @Column(name = "blue_remaining", columnDefinition = "int2")
    private int blueRemaining;

    @Enumerated
    @Column(name = "current_turn", columnDefinition = "int2")
    private Color currentTurn;

    public GameInstance(String name, List<Card> cards, GameStatus status, int redRemaining, int blueRemaining, Color currentTurn) {
        this.name = name;
        this.cards = cards;
        this.status = status;
        this.redRemaining = redRemaining;
        this.blueRemaining = blueRemaining;
        this.currentTurn = currentTurn;
    }

    @Override
    public String toString() {
        return String.format("Game [%s %s R:%d B:%d %s]", name, status, redRemaining, blueRemaining, currentTurn);
    }

    public Card findByIndex(int index) {
        for(Card card : cards) {
            if(card.getIndex() == index) {
                return card;
            }
        }
        return null;
    }

}
