package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.service.BatterCrawlingService;
import database.architecture.backend.domain.crawling.service.PitcherCrawlingService;
import database.architecture.backend.domain.crawling.service.PlayerCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling/pitcher")
public class PitcherCrawlingController {
    private final PlayerCrawlingService playerCrawlingService;
    private final PitcherCrawlingService pitcherCrawlingService;

    @GetMapping("/{name}/id")
    public ResponseEntity<?> getPlayerId(@PathVariable String name) {
        try{
            return ResponseEntity.ok(playerCrawlingService.getPlayerId(name));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/info")
    public ResponseEntity<?> getPlayerInfo(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(playerCrawlingService.getPlayerInfo(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/stats")
    public ResponseEntity<?> getBatterStats(@PathVariable int playerId) throws IOException {
        try{
            return ResponseEntity.ok(pitcherCrawlingService.getPitcherStats(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/zone")
    public ResponseEntity<?> crawlingBatterZone(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(pitcherCrawlingService.getPitcherZoneStats(playerId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
