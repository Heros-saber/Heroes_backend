package database.architecture.backend.domain.analysis.pitcher.dto;

import database.architecture.backend.domain.analysis.batter.dto.BatterAnalysisDTO;
import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.PitcherZoneStat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class PitcherAnalysisDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PitcherAnalyzeResponse{
        private List<PitcherAnalysisDTO.PitcherZoneDTO> zoneStatDTO;
        private String one_line_analysis;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PitcherZoneDTO{
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

        public void setTitle(String title) {this.circumstance = title;}
        public void setTag(String tag) {this.tag = tag;}
        public PitcherZoneDTO(PitcherZoneStat pitcherZoneStat){
            if(pitcherZoneStat.getTag().equals("타율"))
                this.tag = "피안타율";
            else
                this.tag = pitcherZoneStat.getTag();
            this.circumstance = pitcherZoneStat.getCircumstance() + " " + this.tag;
            this.zone1 = pitcherZoneStat.getZone1();
            this.zone2 = pitcherZoneStat.getZone2();
            this.zone3 = pitcherZoneStat.getZone3();
            this.zone4 = pitcherZoneStat.getZone4();
            this.zone5 = pitcherZoneStat.getZone5();
            this.zone6 = pitcherZoneStat.getZone6();
            this.zone7 = pitcherZoneStat.getZone7();
            this.zone8 = pitcherZoneStat.getZone8();
            this.zone9 = pitcherZoneStat.getZone9();
            this.zone10 = pitcherZoneStat.getZone10();
            this.zone11 = pitcherZoneStat.getZone11();
            this.zone12 = pitcherZoneStat.getZone12();
            this.zone13 = pitcherZoneStat.getZone13();
            this.zone14 = pitcherZoneStat.getZone14();
            this.zone15 = pitcherZoneStat.getZone15();
            this.zone16 = pitcherZoneStat.getZone16();
            this.zone17 = pitcherZoneStat.getZone17();
            this.zone18 = pitcherZoneStat.getZone18();
            this.zone19 = pitcherZoneStat.getZone19();
            this.zone20 = pitcherZoneStat.getZone20();
            this.zone21 = pitcherZoneStat.getZone21();
            this.zone22 = pitcherZoneStat.getZone22();
            this.zone23 = pitcherZoneStat.getZone23();
            this.zone24 = pitcherZoneStat.getZone24();
            this.zone25 = pitcherZoneStat.getZone25();
        }
    }
}
