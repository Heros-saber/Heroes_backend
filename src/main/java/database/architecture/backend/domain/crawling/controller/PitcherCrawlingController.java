package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.service.BatterCrawlingService;
import database.architecture.backend.domain.crawling.service.PitcherCrawlingService;
import database.architecture.backend.domain.crawling.service.PlayerCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling/pitcher")
public class PitcherCrawlingController {
    private final PlayerCrawlingService playerCrawlingService;
    private final PitcherCrawlingService pitcherCrawlingService;

    @PostMapping("/{name}")
    public ResponseEntity<?> saveBatter(@PathVariable String name) {
        try{
            return ResponseEntity.ok(pitcherCrawlingService.savePitcher(name));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
    public ResponseEntity<?> getPitcherStats(@PathVariable int playerId) throws IOException {
        try{
            return ResponseEntity.ok(pitcherCrawlingService.getPitcherStats(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/zone")
    public ResponseEntity<?> crawlingPitcherZone(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(pitcherCrawlingService.getPitcherZoneStats(playerId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
