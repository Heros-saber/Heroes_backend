package database.architecture.backend.domain.crawling.dto;

import lombok.Data;

@Data
public class BatterStatsDTO {
    private String year;
    private float hAvg;
    private float obp;
    private float slg;
    private float ops;
    private float wrcPlus;
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
    private float war;
}