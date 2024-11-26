package database.architecture.backend.domain.api.controller;

import database.architecture.backend.domain.api.dto.MainPageDTO;
import database.architecture.backend.domain.api.service.MainPageService;
import database.architecture.backend.domain.entity.MatchRecord;
import database.architecture.backend.domain.repository.MatchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;
    private final MatchRecordRepository matchRecordRepository;

    @GetMapping("/main")
    public ResponseEntity<?> getMainPage() {
        MainPageDTO.MainPageListDTO mainPageDTO = mainPageService.getMainPageData();
        return ResponseEntity.ok().body(mainPageDTO);
    }

    @GetMapping("/main/match")
    public ResponseEntity<List<MainPageDTO.MatchRecordDTO>> getMonthlyMatches(@RequestParam(value = "year") int year,
                                                                              @RequestParam(value = "month") int month) {
        List<MainPageDTO.MatchRecordDTO> matchRecords = mainPageService.getMonthlyMatches(year, month);
        return ResponseEntity.ok().body(matchRecords);
    }
}
