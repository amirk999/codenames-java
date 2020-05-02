package com.klar.codenames.game.controller;

import com.klar.codenames.game.dto.GameInstanceDTO;
import com.klar.codenames.game.model.GameInstance;
import com.klar.codenames.game.service.GameInstanceService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GameInstanceController {
    private final GameInstanceService service;

    public GameInstanceController(GameInstanceService service) {
        this.service = service;
    }

    @PostMapping("/games")
    public GameInstanceDTO createGame(@RequestBody GameInstance game) throws Exception {
        return new GameInstanceDTO(service.create(game));
    }

    @PostMapping("/games/find")
    public Map<String, Long> findGame(@RequestBody Map<String, String> body) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("id", service.findIdByName(body.get("name")));
        return map;
    }

    @MessageMapping("/play/{id}")
    @SendTo("/game/status/{id}")
    public GameInstanceDTO playCard(@DestinationVariable Long id, @RequestBody Map<String, String> body) throws Exception {
        int card = Integer.parseInt(body.get("card"));
//        long id = Long.parseLong(body.get("game"));
        return new GameInstanceDTO(service.playCard(card, id));
    }

    @SubscribeMapping("/game/join/{id}")
    public GameInstanceDTO userConnected(@DestinationVariable Long id) throws Exception {
        return new GameInstanceDTO(service.findById(id));
    }

    // TODO: may need to remove the below endpoints

    @GetMapping("/games")
    public List<GameInstance> getAllGames() {
        return service.findAll();
    }

    @GetMapping("/games/{id}")
    public GameInstanceDTO getOneGame(@PathVariable Long id) {
        return new GameInstanceDTO(service.findById(id));
    }

    @DeleteMapping("/games/{id}")
    public GameInstance deleteGame(@PathVariable Long id) throws Exception {
        return service.deleteById(id);
    }
}
