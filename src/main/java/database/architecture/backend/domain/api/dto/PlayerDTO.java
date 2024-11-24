package database.architecture.backend.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PlayerDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BatterResponseDTO {
        private PlayerInfoDTO playerInfo;
        private List<BatterStatDTO> stats;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PitcherResponseDTO {
        private PlayerInfoDTO playerInfo;
        private List<PitcherStatDTO> stats;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerInfoDTO {

        private String playerName;
        private LocalDate playerBorn;
        private String playerDraft;
        private String playerPos;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BatterStatDTO {
        private String year;
        private Double avg;
        private Double ops;
        private Double slg;
        private Double obp;
        private Double war;
        private int pa;
        private int ab;
        private int hit;
        private int twoB;
        private int threeB;
        private int hr;
        private int rbi;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PitcherStatDTO {
        private String year;
        private int games;
        private int win;
        private int lose;
        private int save;
        private int hold;
        private double ip;
        private double era;
        private int tbf;
        private int h;
        private int twoB;
        private int threeB;
        private int hr;
        private int bb;
        private int so;
        private double whip;
        private double war;
    }
}

