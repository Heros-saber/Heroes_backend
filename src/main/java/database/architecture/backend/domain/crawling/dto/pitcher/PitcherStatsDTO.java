package database.architecture.backend.domain.crawling.dto.pitcher;

import lombok.Data;

@Data
public class PitcherStatsDTO {
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
