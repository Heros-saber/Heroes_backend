package database.architecture.backend.domain.crawling.dto.batter;

import database.architecture.backend.domain.entity.BatterStat;
import database.architecture.backend.domain.entity.Player;
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

    public BatterStat toEntity(Player player){
        return BatterStat.builder().year(year).avg(hAvg).obp(obp).slg(slg).ops(ops).wrcPlus(wrcPlus).hit(h).h_double(twoB)
                .triple(threeB).hr(hr).rbi(rbi).sb(sb).bb(bb).so(so).pa(pa).ab(ab).war(war).player(player).build();
    }
}