package database.architecture.backend.domain.analy.batter.service;

import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.repository.BatterZoneStatRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatterAnalysisService {
    private final BatterZoneStatRepository zoneStatRepository;
    private final PlayerRepository playerRepository;

    public String one_line_analysis(String name){
        Player player = playerRepository.findPlayerByPlayerName(name);
        List<BatterZoneStat> count = zoneStatRepository.findAllByPlayerAndCircumstance(player, "카운트");
        List<BatterZoneStat> finishing = zoneStatRepository.findAllByPlayerAndCircumstance(player, "결정구");
        List<BatterZoneStat> overall = zoneStatRepository.findAllByPlayerAndCircumstance(player, "전체");
        List<BatterZoneStat> vs_right = zoneStatRepository.findAllByPlayerAndCircumstance(player, "우투");
        List<BatterZoneStat> vs_left = zoneStatRepository.findAllByPlayerAndCircumstance(player, "좌투");

        return "";
    }

    private String crisis_analyze(List<BatterZoneStat> count, List<BatterZoneStat> finishing){
        for (BatterZoneStat batterZoneStat : count) {

        }
        return "";
    }
}
