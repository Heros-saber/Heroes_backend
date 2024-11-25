package database.architecture.backend.domain.analysis.batter.controller;

import database.architecture.backend.domain.analysis.batter.service.BatterAnalysisService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batter/analysis")
public class BatterAnalysisController {
    private final BatterAnalysisService service;
    @GetMapping("/{name}")
    public ResponseEntity<?> test(@PathVariable String name){

        return ResponseEntity.ok(service.one_line_analysis(name));
    }

}
