package database.architecture.backend.domain.analysis.pitcher.service;

import database.architecture.backend.domain.entity.PitcherStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.repository.PitcherStatRepository;
import database.architecture.backend.domain.repository.PitcherZoneStatRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PitcherAnalysisService {
    private final PlayerRepository playerRepository;
    private final PitcherStatRepository statRepository;
    private final PitcherZoneStatRepository zoneStatRepository;

    public String analyzePitcher(String name){
        Player player = playerRepository.findPlayerByPlayerName(name);
        PitcherStat overall = statRepository.findPitcherStatByPlayerAndYear(player, "2024");
        return pitcherLevel(overall);
    }

    public String pitcherLevel(PitcherStat overall){
        Double era = overall.getEra();

        if(era <= 3.5){
            return "리그 최정상급 투수입니다.\n";
        }else if (era <= 4.5){
            return "리그 정상급 투수입니다.\n";
        }else if (era <= 5.0){
            return "리그 평균 투수입니다.\n";
        }else if (era <= 6.5){
            return "리그 하위권 투수입니다.\n";
        }else {
            return "리그 최하위권 투수입니다.\n";
        }
    }
}
