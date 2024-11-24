package database.architecture.backend.domain.api.service;

import database.architecture.backend.domain.api.dto.PlayerDTO;
import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.entity.*;
import database.architecture.backend.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerInfoService {

    private final PlayerInfoRepository playerInfoRepository;
    private final BatterStatRepository batterStatRepository;
    private final PitcherStatRepository pitcherStatRepository;
    private final BatterZoneStatRepository batterZoneStatRepository;
    private final PitcherZoneStatRepository pitcherZoneStatRepository;

    public Player getPlayerByName(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        if (player == null) {
            throw new IllegalArgumentException("선수를 찾을 수 없습니다: " + playerName);
        }
        return player;
    }

    public PlayerDTO.PlayerInfoDTO getPlayerInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        return PlayerDTO.PlayerInfoDTO.builder()
                .playerName(player.getPlayerName())
                .playerBorn(player.getPlayerBorn())
                .playerDraft(player.getPlayerDraft())
                .playerPos(parsePosition(player.getPlayerPos()))
                .build();
    }

    public PlayerDTO.BatterResponseDTO getBatterInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);

        PlayerDTO.PlayerInfoDTO playerInfo = getPlayerInfo(playerName);

        List<BatterStat> batterStats = batterStatRepository.findAllByPlayer(player);
        List<PlayerDTO.BatterStatDTO> stats = batterStats.stream()
                .map(stat -> PlayerDTO.BatterStatDTO.builder()
                        .year(stat.getYear())
                        .avg(stat.getAvg())
                        .ops(stat.getOps())
                        .slg(stat.getSlg())
                        .obp(stat.getObp())
                        .war(stat.getWar())
                        .pa(stat.getPa())
                        .ab(stat.getAb())
                        .hit(stat.getHit())
                        .twoB(stat.getH_double())
                        .threeB(stat.getTriple())
                        .hr(stat.getHr())
                        .rbi(stat.getRbi())
                        .wrcPlus(stat.getWrcPlus())
                        .build())
                .toList();

        List<BatterZoneStat> batterZoneStats = batterZoneStatRepository.findAllByPlayer(player);
        List<PlayerDTO.ZoneStatDTO> filteredZones = batterZoneStats.stream()
                .filter(zone -> "타율".equals(zone.getTag()) && "전체".equals(zone.getCircumstance()) || "ops".equals(zone.getTag()) && "전체".equals(zone.getCircumstance()))
                .map(zone -> PlayerDTO.ZoneStatDTO.builder()
                        .tag(zone.getTag())
                        .circumstance(zone.getCircumstance())
                        .zone1(zone.getZone1())
                        .zone2(zone.getZone2())
                        .zone3(zone.getZone3())
                        .zone4(zone.getZone4())
                        .zone5(zone.getZone5())
                        .zone6(zone.getZone6())
                        .zone7(zone.getZone7())
                        .zone8(zone.getZone8())
                        .zone9(zone.getZone9())
                        .zone10(zone.getZone10())
                        .zone11(zone.getZone11())
                        .zone12(zone.getZone12())
                        .zone13(zone.getZone13())
                        .zone14(zone.getZone14())
                        .zone15(zone.getZone15())
                        .zone16(zone.getZone16())
                        .zone17(zone.getZone17())
                        .zone18(zone.getZone18())
                        .zone19(zone.getZone19())
                        .zone20(zone.getZone20())
                        .zone21(zone.getZone21())
                        .zone22(zone.getZone22())
                        .zone23(zone.getZone23())
                        .zone24(zone.getZone24())
                        .zone25(zone.getZone25())
                        .build())
                .toList();

        return PlayerDTO.BatterResponseDTO.builder()
                .playerInfo(playerInfo)
                .stats(stats)
                .zone(filteredZones)
                .build();
    }

    public PlayerDTO.PitcherResponseDTO getPitcherInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);

        PlayerDTO.PlayerInfoDTO playerInfo = getPlayerInfo(playerName);

        List<PitcherStat> pitcherStats = pitcherStatRepository.findAllByPlayer(player);
        List<PlayerDTO.PitcherStatDTO> stats = pitcherStats.stream()
                .map(stat -> PlayerDTO.PitcherStatDTO.builder()
                        .year(stat.getYear())
                        .games(stat.getGames())
                        .win(stat.getWin())
                        .lose(stat.getLose())
                        .save(stat.getSave())
                        .hold(stat.getHold())
                        .ip(stat.getIp())
                        .era(stat.getEra())
                        .er(stat.getEr())
                        .tbf(stat.getTbf())
                        .h(stat.getHit())
                        .twoB(stat.getH_double())
                        .threeB(stat.getTriple())
                        .hr(stat.getHr())
                        .bb(stat.getBb())
                        .so(stat.getSo())
                        .whip(stat.getWhip())
                        .war(stat.getWar())
                        .build())
                .toList();

        List<PitcherZoneStat> pitcherZoneStats = pitcherZoneStatRepository.findAllByPlayer(player);
        List<PlayerDTO.ZoneStatDTO> filteredZones = pitcherZoneStats.stream()
                .filter(zone -> "구사율".equals(zone.getTag()) && "전체".equals(zone.getCircumstance()) || "타율".equals(zone.getTag()) && "전체".equals(zone.getCircumstance()))
                .map(zone -> PlayerDTO.ZoneStatDTO.builder()
                        .tag(zone.getTag())
                        .circumstance(zone.getCircumstance())
                        .zone1(zone.getZone1())
                        .zone2(zone.getZone2())
                        .zone3(zone.getZone3())
                        .zone4(zone.getZone4())
                        .zone5(zone.getZone5())
                        .zone6(zone.getZone6())
                        .zone7(zone.getZone7())
                        .zone8(zone.getZone8())
                        .zone9(zone.getZone9())
                        .zone10(zone.getZone10())
                        .zone11(zone.getZone11())
                        .zone12(zone.getZone12())
                        .zone13(zone.getZone13())
                        .zone14(zone.getZone14())
                        .zone15(zone.getZone15())
                        .zone16(zone.getZone16())
                        .zone17(zone.getZone17())
                        .zone18(zone.getZone18())
                        .zone19(zone.getZone19())
                        .zone20(zone.getZone20())
                        .zone21(zone.getZone21())
                        .zone22(zone.getZone22())
                        .zone23(zone.getZone23())
                        .zone24(zone.getZone24())
                        .zone25(zone.getZone25())
                        .build())
                .toList();

        return PlayerDTO.PitcherResponseDTO.builder()
                .playerInfo(playerInfo)
                .stats(stats)
                .zone(filteredZones)
                .build();
    }

    public PlayerDTO.BatterDetailResponseDTO getBatterDetailInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        PlayerDTO.PlayerInfoDTO playerInfo = getPlayerInfo(playerName);

        // circumstance별 그룹화된 데이터를 포함
        List<BatterZoneStat> batterZoneStats = batterZoneStatRepository.findAllByPlayer(player);
        Map<String, List<PlayerDTO.ZoneStatDTO>> groupedZones = batterZoneStats.stream()
                .collect(Collectors.groupingBy(
                        BatterZoneStat::getCircumstance,
                        Collectors.mapping(zone -> PlayerDTO.ZoneStatDTO.builder()
                                        .tag(zone.getTag())
                                        .circumstance(zone.getCircumstance())
                                        .zone1(zone.getZone1())
                                        .zone2(zone.getZone2())
                                        .zone3(zone.getZone3())
                                        .zone4(zone.getZone4())
                                        .zone5(zone.getZone5())
                                        .zone6(zone.getZone6())
                                        .zone7(zone.getZone7())
                                        .zone8(zone.getZone8())
                                        .zone9(zone.getZone9())
                                        .zone10(zone.getZone10())
                                        .zone11(zone.getZone11())
                                        .zone12(zone.getZone12())
                                        .zone13(zone.getZone13())
                                        .zone14(zone.getZone14())
                                        .zone15(zone.getZone15())
                                        .zone16(zone.getZone16())
                                        .zone17(zone.getZone17())
                                        .zone18(zone.getZone18())
                                        .zone19(zone.getZone19())
                                        .zone20(zone.getZone20())
                                        .zone21(zone.getZone21())
                                        .zone22(zone.getZone22())
                                        .zone23(zone.getZone23())
                                        .zone24(zone.getZone24())
                                        .zone25(zone.getZone25())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        return PlayerDTO.BatterDetailResponseDTO.builder()
                .playerInfo(playerInfo)
                .groupedZones(groupedZones)
                .build();
    }


    public PlayerDTO.PitcherDetailResponseDTO getPitcherDetailInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        PlayerDTO.PlayerInfoDTO playerInfo = getPlayerInfo(playerName);

        List<PitcherZoneStat> pitcherZoneStats = pitcherZoneStatRepository.findAllByPlayer(player);
        Map<String, List<PlayerDTO.ZoneStatDTO>> groupedZones = pitcherZoneStats.stream()
                .collect(Collectors.groupingBy(
                        PitcherZoneStat::getCircumstance,
                        Collectors.mapping(zone -> PlayerDTO.ZoneStatDTO.builder()
                                        .tag(zone.getTag())
                                        .circumstance(zone.getCircumstance())
                                        .zone1(zone.getZone1())
                                        .zone2(zone.getZone2())
                                        .zone3(zone.getZone3())
                                        .zone4(zone.getZone4())
                                        .zone5(zone.getZone5())
                                        .zone6(zone.getZone6())
                                        .zone7(zone.getZone7())
                                        .zone8(zone.getZone8())
                                        .zone9(zone.getZone9())
                                        .zone10(zone.getZone10())
                                        .zone11(zone.getZone11())
                                        .zone12(zone.getZone12())
                                        .zone13(zone.getZone13())
                                        .zone14(zone.getZone14())
                                        .zone15(zone.getZone15())
                                        .zone16(zone.getZone16())
                                        .zone17(zone.getZone17())
                                        .zone18(zone.getZone18())
                                        .zone19(zone.getZone19())
                                        .zone20(zone.getZone20())
                                        .zone21(zone.getZone21())
                                        .zone22(zone.getZone22())
                                        .zone23(zone.getZone23())
                                        .zone24(zone.getZone24())
                                        .zone25(zone.getZone25())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        return PlayerDTO.PitcherDetailResponseDTO.builder()
                .playerInfo(playerInfo)
                .groupedZones(groupedZones)
                .build();
    }


    private String parsePosition(Integer posCode) {
        Map<Integer, String> positionMap = Map.of(
                1, "P", 2, "C", 3, "1B", 4, "2B", 5, "3B",
                6, "SS", 7, "LF", 8, "CF", 9, "RF", 10, "DH"
        );
        return positionMap.getOrDefault(posCode, "Unknown");
    }
}
