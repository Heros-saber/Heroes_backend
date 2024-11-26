package database.architecture.backend.domain.analysis.batter.dto;

import database.architecture.backend.domain.entity.BatterZoneStat;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class BatterAnalysisDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BatterAnalyzeResponse{
        private List<BatterZoneDTO> zoneStatDTO;
        private String one_line_analysis;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BatterZoneDTO{
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
        public BatterZoneDTO(BatterZoneStat batterZoneStat){
            this.tag = batterZoneStat.getTag();
            this.circumstance = batterZoneStat.getCircumstance() + " 타율";
            this.zone1 = batterZoneStat.getZone1();
            this.zone2 = batterZoneStat.getZone2();
            this.zone3 = batterZoneStat.getZone3();
            this.zone4 = batterZoneStat.getZone4();
            this.zone5 = batterZoneStat.getZone5();
            this.zone6 = batterZoneStat.getZone6();
            this.zone7 = batterZoneStat.getZone7();
            this.zone8 = batterZoneStat.getZone8();
            this.zone9 = batterZoneStat.getZone9();
            this.zone10 = batterZoneStat.getZone10();
            this.zone11 = batterZoneStat.getZone11();
            this.zone12 = batterZoneStat.getZone12();
            this.zone13 = batterZoneStat.getZone13();
            this.zone14 = batterZoneStat.getZone14();
            this.zone15 = batterZoneStat.getZone15();
            this.zone16 = batterZoneStat.getZone16();
            this.zone17 = batterZoneStat.getZone17();
            this.zone18 = batterZoneStat.getZone18();
            this.zone19 = batterZoneStat.getZone19();
            this.zone20 = batterZoneStat.getZone20();
            this.zone21 = batterZoneStat.getZone21();
            this.zone22 = batterZoneStat.getZone22();
            this.zone23 = batterZoneStat.getZone23();
            this.zone24 = batterZoneStat.getZone24();
            this.zone25 = batterZoneStat.getZone25();
        }

        public BatterZoneDTO(BatterZoneStat swingStat, BatterZoneStat contactStat) {
            this.tag = "확률";
            this.circumstance = swingStat.getCircumstance() + " 투구 시 스트라이크 확률";

            List<Double> calculatedList = new ArrayList<>();

            for (int i = 0; i < 25; i++) {
                final int index = i + 1;

                Double calculatedZone = 0.0;
                try {
                    Double swingValue = (Double) BatterZoneStat.class.getMethod("getZone" + index).invoke(swingStat) / 100;
                    Double contactValue = (Double) BatterZoneStat.class.getMethod("getZone" + index).invoke(contactStat) / 100;

                    swingValue = (swingValue == null) ? 1.0 : swingValue;
                    contactValue = (contactValue == null) ? 1.0 : contactValue;

                    calculatedZone = (1 - swingValue) + swingValue * (1 - contactValue);

                } catch (Exception e) {
                    calculatedZone = 0.0;
                }
                calculatedList.add(Double.parseDouble(String.format("%.2f", calculatedZone * 100)));
            }

            this.zone1 = calculatedList.get(0);
            this.zone2 = calculatedList.get(1);
            this.zone3 = calculatedList.get(2);
            this.zone4 = calculatedList.get(3);
            this.zone5 = calculatedList.get(4);
            this.zone6 = calculatedList.get(5);
            this.zone7 = calculatedList.get(6);
            this.zone8 = calculatedList.get(7);
            this.zone9 = calculatedList.get(8);
            this.zone10 = calculatedList.get(9);
            this.zone11 = calculatedList.get(10);
            this.zone12 = calculatedList.get(11);
            this.zone13 = calculatedList.get(12);
            this.zone14 = calculatedList.get(13);
            this.zone15 = calculatedList.get(14);
            this.zone16 = calculatedList.get(15);
            this.zone17 = calculatedList.get(16);
            this.zone18 = calculatedList.get(17);
            this.zone19 = calculatedList.get(18);
            this.zone20 = calculatedList.get(19);
            this.zone21 = calculatedList.get(20);
            this.zone22 = calculatedList.get(21);
            this.zone23 = calculatedList.get(22);
            this.zone24 = calculatedList.get(23);
            this.zone25 = calculatedList.get(24);
        }
    }

}
