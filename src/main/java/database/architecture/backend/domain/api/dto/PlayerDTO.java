package database.architecture.backend.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PlayerDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BatterResponseDTO {
        private PlayerInfoDTO playerInfo;
        private List<BatterStatDTO> stats;
        private List<ZoneStatDTO> zone;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PitcherResponseDTO {
        private PlayerInfoDTO playerInfo;
        private List<PitcherStatDTO> stats;
        private List<ZoneStatDTO> zone;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BatterDetailResponseDTO {
        private PlayerInfoDTO playerInfo;
        private Map<String, List<ZoneStatDTO>> groupedZones;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PitcherDetailResponseDTO {
        private PlayerInfoDTO playerInfo;
        private Map<String, List<ZoneStatDTO>> groupedZones;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PlayerInfoDTO {

        private String playerName;
        private LocalDate playerBorn;
        private String playerDraft;
        private String playerPos;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
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
        private Double wrcPlus;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PitcherStatDTO {
        private String year;
        private int games;
        private int win;
        private int lose;
        private int save;
        private int hold;
        private double ip;
        private double era;
        private int er;
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

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ZoneStatDTO {
        private String tag;
        private String circumstance;
        private Double zone1;
        private Double zone2;
        private Double zone3;
        private Double zone4;
        private Double zone5;
        private Double zone6;
        private Double zone7;
        private Double zone8;
        private Double zone9;
        private Double zone10;
        private Double zone11;
        private Double zone12;
        private Double zone13;
        private Double zone14;
        private Double zone15;
        private Double zone16;
        private Double zone17;
        private Double zone18;
        private Double zone19;
        private Double zone20;
        private Double zone21;
        private Double zone22;
        private Double zone23;
        private Double zone24;
        private Double zone25;
    }
}

