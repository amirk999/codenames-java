package com.klar.codenames.game.repository;

import com.klar.codenames.game.model.Card;
import com.klar.codenames.game.model.GameInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameInstanceRepository extends JpaRepository<GameInstance, Long> {

    @Query("SELECT c FROM Card c WHERE c.game = ?1 AND c.index = ?2")
    Card findCardByIndex(GameInstance game, int index);

    GameInstance findByName(String name);
}
