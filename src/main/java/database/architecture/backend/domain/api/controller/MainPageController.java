package database.architecture.backend.domain.api.controller;

import database.architecture.backend.domain.api.dto.MainPageDTO;
import database.architecture.backend.domain.api.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping("/main")
    public ResponseEntity<?> getMainPage() {
        MainPageDTO mainPageDTO = mainPageService.getMainPageData();
        return ResponseEntity.ok().body(mainPageDTO);
    }
}
