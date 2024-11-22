package database.architecture.backend.domain.crawling.dto.pitcher;

import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.PitcherZoneStat;
import database.architecture.backend.domain.entity.Player;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PitcherZoneDTO {
    private String circumstance;
    private String label;
    private List<Double> data;

    public PitcherZoneStat toEntity(Player player){
        return PitcherZoneStat.builder().circumstance(circumstance.split(" ")[2]).tag(label)
                .zone1(data.get(0)).zone2(data.get(1)).zone3(data.get(2)).zone4(data.get(3)).zone5(data.get(4))
                .zone6(data.get(5)).zone7(data.get(6)).zone8(data.get(7)).zone9(data.get(8)).zone10(data.get(9))
                .zone11(data.get(10)).zone12(data.get(11)).zone13(data.get(12)).zone14(data.get(13)).zone15(data.get(14))
                .zone16(data.get(15)).zone17(data.get(16)).zone18(data.get(17)).zone19(data.get(18)).zone20(data.get(19))
                .zone21(data.get(20)).zone22(data.get(21)).zone23(data.get(22)).zone24(data.get(23)).zone25(data.get(24))
                .player(player).year(2024).build();
    }

}
