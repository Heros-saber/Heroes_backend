package database.architecture.backend.domain.api.service;

import database.architecture.backend.domain.api.dto.PlayerDTO;
import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.entity.BatterStat;
import database.architecture.backend.domain.entity.PitcherStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.repository.BatterStatRepository;
import database.architecture.backend.domain.repository.PitcherStatRepository;
import database.architecture.backend.domain.repository.PlayerInfoRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerInfoService {

    private final PlayerInfoRepository playerInfoRepository;
    private final BatterStatRepository batterStatRepository;
    private final PitcherStatRepository pitcherStatRepository;

    public PlayerDTO.BatterResponseDTO getBatterInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        if (player == null)
            throw new IllegalArgumentException("선수를 찾을 수 없습니다: " + playerName);

        PlayerDTO.PlayerInfoDTO playerInfo = PlayerDTO.PlayerInfoDTO.builder()
                .playerName(player.getPlayerName())
                .playerBorn(player.getPlayerBorn())
                .playerDraft(player.getPlayerDraft())
                .playerPos(parsePosition(player.getPlayerPos()))
                .build();

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
                        .build())
                .toList();

        return PlayerDTO.BatterResponseDTO.builder()
                .playerInfo(playerInfo)
                .stats(stats)
                .build();
    }

    public PlayerDTO.PitcherResponseDTO getPitcherInfo(String playerName) {
        Player player = playerInfoRepository.findPlayerByPlayerName(playerName);
        if (player == null)
            throw new IllegalArgumentException("선수를 찾을 수 없습니다: " + playerName);

        PlayerDTO.PlayerInfoDTO playerInfoDTO = PlayerDTO.PlayerInfoDTO.builder()
                .playerName(player.getPlayerName())
                .playerBorn(player.getPlayerBorn())
                .playerDraft(player.getPlayerDraft())
                .playerPos(parsePosition(player.getPlayerPos()))
                .build();

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

        return PlayerDTO.PitcherResponseDTO.builder()
                .playerInfo(playerInfoDTO)
                .stats(stats)
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
