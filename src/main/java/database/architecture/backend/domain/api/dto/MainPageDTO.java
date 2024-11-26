package database.architecture.backend.domain.api.dto;

import database.architecture.backend.domain.entity.MatchRecord;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MainPageDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class MainPageListDTO {
        private int rank;
        private int win;
        private int lose;
        private String nextGame;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchRecordDTO {
        private LocalDate date;
        private Integer kiwoomScore;
        private Integer opponentScore;
        private Boolean win;
    }
}
