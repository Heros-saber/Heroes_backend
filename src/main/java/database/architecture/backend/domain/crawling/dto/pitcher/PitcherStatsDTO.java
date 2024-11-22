package database.architecture.backend.domain.crawling.dto.pitcher;

import database.architecture.backend.domain.entity.PitcherStat;
import database.architecture.backend.domain.entity.Player;
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

    public PitcherStat toEntity(Player player){
        return PitcherStat.builder().year(year).games(games).win(win).lose(lose).save(save).hold(hold).ip(ip).era(era).er(er)
                .tbf(tbf).hit(h).h_double(twoB).triple(threeB).hr(hr).bb(bb).so(so).whip(whip).war(war).player(player).build();
    }
}
