package com.klar.codenames.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "int2")
    private int index;

    @Enumerated
    @Column(columnDefinition = "int2")
    private Color color;

    private String body;

    @Enumerated
    @Column(columnDefinition = "int2")
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private GameInstance game;

    public Card(int index, Color color, String body, CardStatus status, GameInstance game) {
        super();
        this.index = index;
        this.color = color;
        this.body = body;
        this.status = status;
        this.game = game;
    }

    @Override
    public String toString() {
        return String.format("Card [%d %s %s %s]", index, body, color, status);
    }

}
