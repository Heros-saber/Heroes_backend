package database.architecture.backend.domain.crawling.dto.batter;

import lombok.Data;

@Data
public class BatterStatsDTO {
    private String year;
    private double hAvg;
    private double obp;
    private double slg;
    private double ops;
    private double wrcPlus;
    private int h;
    private int twoB;
    private int threeB;
    private int hr;
    private int rbi;
    private int sb;
    private int bb;
    private int so;
    private int pa;
    private int ab;
    private double war;
}