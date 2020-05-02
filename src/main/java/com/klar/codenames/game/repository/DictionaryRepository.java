package com.klar.codenames.game.repository;

import com.klar.codenames.game.model.GameInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictionaryRepository extends JpaRepository<GameInstance, Long> {

    @Query(value="SELECT word FROM dictionary OFFSET floor(random()*(SELECT COUNT(1) FROM dictionary)) LIMIT ?1", nativeQuery=true)
    List<String> findRandom(int num);
}
