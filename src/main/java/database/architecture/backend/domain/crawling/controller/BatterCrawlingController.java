package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterStatsDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterZoneDTO;
import database.architecture.backend.domain.crawling.service.BatterCrawlingService;
import database.architecture.backend.domain.crawling.service.PlayerCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling/batter")
public class BatterCrawlingController {
    private final PlayerCrawlingService playerCrawlingService;
    private final BatterCrawlingService batterCrawlingService;

    @PostMapping("/{name}")
    public ResponseEntity<?> saveBatter(@PathVariable String name) {
        try{
            return ResponseEntity.ok(batterCrawlingService.saveBatter(name));
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
    public ResponseEntity<?> getBatterStats(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(batterCrawlingService.getBatterStats(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/zone")
    public ResponseEntity<?> crawlingBatterZone(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(batterCrawlingService.getBatterZoneStats(playerId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
