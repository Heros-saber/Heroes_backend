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
            gameCrawlingService.gameCrawling(year, month);
            return ResponseEntity.ok("경기 결과, 일정 크롤링 성공");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/team_rank")
    public ResponseEntity<?> rankCrawling() {
        try{
            gameCrawlingService.getTeamRank();
            return ResponseEntity.ok().body("팀 순위 크롤링 성공");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
