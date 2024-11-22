package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.service.BatterCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling/batter")
public class BatterCrawlingController {
    private final BatterCrawlingService batterCrawlingService;

    @GetMapping("/{name}/id")
    public ResponseEntity<?> getPlayerId(@PathVariable String name) {
        try{
            return ResponseEntity.ok(batterCrawlingService.getPlayerId(name));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/info")
    public ResponseEntity<?> getPlayerInfo(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(batterCrawlingService.getPlayerInfo(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/stats")
    public ResponseEntity<?> getPlayerStats(@PathVariable int playerId) throws IOException {
        try{
            return ResponseEntity.ok(batterCrawlingService.getPlayerStats(playerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}/zone")
    public ResponseEntity<?> crawlAndProcess(@PathVariable int playerId) {
        try{
            return ResponseEntity.ok(batterCrawlingService.getBatterZoneStats(playerId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
