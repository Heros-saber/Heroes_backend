package database.architecture.backend.domain.analysis.pitcher.controller;

import database.architecture.backend.domain.analysis.pitcher.service.PitcherAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pitcher/analysis")
@RequiredArgsConstructor
public class PitcherAnalysisController {
    private final PitcherAnalysisService analysisService;

    @GetMapping("/{name}")
    public ResponseEntity<?> analyzePitcher(@PathVariable String name){
        try{
            return ResponseEntity.ok(analysisService.analyzePitcher(name));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
