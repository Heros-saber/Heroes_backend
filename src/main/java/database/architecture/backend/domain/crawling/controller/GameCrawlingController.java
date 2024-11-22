package database.architecture.backend.domain.crawling.controller;

import database.architecture.backend.domain.crawling.service.GameResultCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling/game")
public class GameCrawlingController {
    private final GameResultCrawlingService gameCrawlingService;
    @GetMapping("/{year}/{month}")
    public ResponseEntity<?> gameCrawling(@PathVariable int year, @PathVariable int month) {
        try{
            return ResponseEntity.ok(gameCrawlingService.gameCrawling(year, month));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/team_rank")
    public ResponseEntity<?> rankCrawling() {
        try{
            return ResponseEntity.ok(gameCrawlingService.getTeamRank());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
