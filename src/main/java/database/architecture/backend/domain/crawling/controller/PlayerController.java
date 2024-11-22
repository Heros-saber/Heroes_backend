package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.PlayerStatsDTO;
import database.architecture.backend.domain.crawling.dto.PlayerZoneDTO;
import database.architecture.backend.domain.crawling.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/id")
    public ResponseEntity<Integer> getPlayerId(@RequestParam String name) {
        int playerId = playerService.getPlayerId(name);
        if (playerId == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(playerId);
        }
        return ResponseEntity.ok(playerId);
    }

    @GetMapping("/info")
    public ResponseEntity<PlayerInfoDTO> getPlayerInfo(@RequestParam int playerId, @RequestParam String playerName) throws IOException {
        PlayerInfoDTO playerInfo = playerService.getPlayerInfo(playerId, playerName);
        return ResponseEntity.ok(playerInfo);
    }

    @GetMapping("/player/stats")
    public List<PlayerStatsDTO> getPlayerStats(@RequestParam int playerId, @RequestParam String playerName) throws IOException {
        return playerService.getPlayerStats(playerId, playerName);
    }

    @PostMapping("/crawlAndProcess")
    public List<PlayerZoneDTO> crawlAndProcess(@RequestParam String playerId, @RequestParam String name) {
        return playerService.playerZoneCrawlingAndPreprocessing(playerId, name);
    }
}
