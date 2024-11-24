package database.architecture.backend.domain.analysis.batter.controller;

import database.architecture.backend.domain.analysis.batter.service.BatterAnalysisService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatterAnalysisController {
    private final BatterAnalysisService service;
    @GetMapping("/test")
    public ResponseEntity<?> test(String name){

        return ResponseEntity.ok(service.one_line_analysis(name));
    }

}
