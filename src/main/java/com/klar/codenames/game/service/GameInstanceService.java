package com.klar.codenames.game.service;

import com.klar.codenames.game.model.*;
import com.klar.codenames.game.repository.DictionaryRepository;
import com.klar.codenames.game.repository.GameInstanceRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameInstanceService {
    private final GameInstanceRepository repository;
    private final DictionaryRepository dictRepository;

    public GameInstanceService(GameInstanceRepository repository, DictionaryRepository dictRepository) {
        this.repository = repository;
        this.dictRepository = dictRepository;
    }

    public List<GameInstance> findAll() {
        return repository.findAll();
    }

    public GameInstance findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    public Long findIdByName(String name) {
        return repository.findByName(name).getId();
    }

    public GameInstance create(GameInstance gameInstance) throws Exception {
        // TODO: allow user to pick game size
        int gameSize = 25;
        int teamMax = 9;
        int blackIndex = -1;
        int currIndex = -1;
        Random rand = new Random();

        GameInstance savedGame = gameInstance;

        if(savedGame == null || savedGame.getName().isEmpty() || repository.findByName(savedGame.getName()) != null) {
            throw new IllegalArgumentException("invalid input");
        }

        List<Card> cards = new ArrayList<Card>();
        List<String> words = dictRepository.findRandom(gameSize);

        savedGame.setRedRemaining(0);
        savedGame.setBlueRemaining(0);

        // Create 25 random cards
        for(int i = 0; i < gameSize; i++) {
            cards.add(new Card(i, Color.Gray, words.get(i), CardStatus.Available, savedGame));
        }

        // Set card colors randomly
        while(blackIndex < 0) {
            currIndex = rand.nextInt(gameSize);

            // Only update gray cards
            if(Color.Gray.equals(cards.get(currIndex).getColor())) {
                if(savedGame.getRedRemaining() < teamMax && savedGame.getBlueRemaining() < teamMax) {
                    if((currIndex % 2) == 0) {
                        // Add red
                        cards.get(currIndex).setColor(Color.Red);
                        savedGame.setRedRemaining(savedGame.getRedRemaining() + 1);
                    } else {
                        // Add blue
                        cards.get(currIndex).setColor(Color.Blue);
                        savedGame.setBlueRemaining(savedGame.getBlueRemaining() + 1);
                    }
                } else if(savedGame.getRedRemaining() == teamMax && savedGame.getBlueRemaining() < (teamMax-1)) {
                    // Red is full. Add this index to blue
                    cards.get(currIndex).setColor(Color.Blue);
                    savedGame.setBlueRemaining(savedGame.getBlueRemaining() + 1);
                } else if(savedGame.getRedRemaining() < (teamMax - 1) && savedGame.getBlueRemaining() == teamMax) {
                    // Blue is full. Add this index to red
                    cards.get(currIndex).setColor(Color.Red);
                    savedGame.setRedRemaining(savedGame.getRedRemaining() + 1);
                } else {
                    // Once the red and blue cards are set, set the next gray card as black
                    cards.get(currIndex).setColor(Color.Black);
                    blackIndex = currIndex;
                }
            }
        }

        // Starting team will have more remaining cards
        if (savedGame.getRedRemaining() > savedGame.getBlueRemaining()) {
            savedGame.setCurrentTurn(Color.Red);
        } else {
            savedGame.setCurrentTurn(Color.Blue);
        }
        // Set default values and cards
        savedGame.setStatus(GameStatus.InProgress);
        savedGame.setCards(cards);

        return repository.save(savedGame);
    }

    public GameInstance playCard(int cardPlayed, Long id) {
        // TODO: add errors if the game or card were not found!
        GameInstance game = findById(id);
        Card cardValue = game.findByIndex(cardPlayed);

        // Only perform an action for available cards. Ignore all other statuses
        if(game.getStatus().equals(GameStatus.InProgress) && cardValue.getStatus().equals(CardStatus.Available)) {
            cardValue.setStatus(CardStatus.Revealed);

            if(cardValue.getColor().equals(Color.Black)) {
                // Black card: the other team wins
                if(game.getCurrentTurn().equals(Color.Blue)) {
                    game.setStatus(GameStatus.RedWon);
                } else {
                    game.setStatus(GameStatus.BlueWon);
                }
            } else {
                // update remaining card counts
                if(cardValue.getColor().equals(Color.Blue)) {
                    game.setBlueRemaining(game.getBlueRemaining() - 1);
                } else if(cardValue.getColor().equals(Color.Red)) {
                    game.setRedRemaining(game.getRedRemaining() - 1);
                }

                // If the remaining is zero for any team, then mark them as the winner
                if(game.getRedRemaining() == 0) {
                    game.setStatus(GameStatus.RedWon);
                } else if(game.getBlueRemaining() == 0) {
                    game.setStatus(GameStatus.BlueWon);
                } else  if (!game.getCurrentTurn().equals(cardValue.getColor())) {
                    // Card does not equal the current turn's color: end turn
                    if (game.getCurrentTurn().equals(Color.Blue)) {
                        game.setCurrentTurn(Color.Red);
                    } else {
                        game.setCurrentTurn(Color.Blue);
                    }
                }
            }
            repository.save(game);
        }

        return game;
    }

    public GameInstance save(GameInstance gameInstance, Long id) throws Exception {
        return repository.findById(id).map(existingGame -> {
            existingGame.setName(gameInstance.getName());
            return repository.save(gameInstance);
        }).orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    public GameInstance deleteById(Long id) throws Exception {
        return repository.findById(id).map(existingGame -> {
            repository.deleteById(id);
            return existingGame;
        }).orElseThrow(() -> new Exception(String.valueOf(id)));
    }
}
