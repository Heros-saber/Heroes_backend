package database.architecture.backend.domain.analysis.pitcher.service;

import database.architecture.backend.domain.analysis.batter.dto.BatterAnalysisDTO;
import database.architecture.backend.domain.analysis.pitcher.dto.PitcherAnalysisDTO;
import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.PitcherStat;
import database.architecture.backend.domain.entity.PitcherZoneStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.repository.PitcherStatRepository;
import database.architecture.backend.domain.repository.PitcherZoneStatRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PitcherAnalysisService {
    private final PlayerRepository playerRepository;
    private final PitcherStatRepository statRepository;
    private final PitcherZoneStatRepository zoneStatRepository;

    public PitcherAnalysisDTO.PitcherAnalyzeResponse analyzePitcher(String name){
        Player player = playerRepository.findPlayerByPlayerName(name);
        String oneLineAnalyze = one_line_analyze(player);
        List<PitcherAnalysisDTO.PitcherZoneDTO> pitcherZoneDTOS = crisisZone(player);
        return PitcherAnalysisDTO.PitcherAnalyzeResponse.builder().one_line_analysis(oneLineAnalyze).zoneStatDTO(pitcherZoneDTOS).build();
    }
    private String one_line_analyze(Player player){
        PitcherStat overall = statRepository.findPitcherStatByPlayerAndYear(player, "2024");
        PitcherZoneStat vsLeft = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "좌타", "타율");
        PitcherZoneStat vsRight = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "우타", "타율");

        PitcherZoneStat count = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "카운트", "구사율");
        PitcherZoneStat crisis = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "결정구", "구사율");

        return pitcherLevel(overall) + vsBatterAnalyze(vsLeft, vsRight) + count2SAnalyze(count, crisis) + count2SZoneAnalyze(player);
    }

    private String pitcherLevel(PitcherStat overall){
        Double era = overall.getEra();

        Integer so = overall.getSo();
        Integer tbf = overall.getTbf();
        Double ip = overall.getIp();

        Double K_percent = Math.round(((double) so / tbf) * 1000) / 10.0;
        double K_9 = Math.round((so / ip) * 90.0) / 10.0;

        String summary = "";
        if(era <= 3.5){
            summary = "리그 최정상급 투수입니다.\n";
        }else if (era <= 4.5){
            summary = "리그 상위권 투수입니다.\n";
        }else if (era <= 5.0){
            summary = "리그 평균 투수입니다.\n";
        }else if (era <= 6.5){
            summary = "리그 하위권 투수입니다.\n";
        }else {
            summary = "리그 최하위권 투수입니다.\n";
        }

        if(K_percent > 20)
            summary += "삼진을 많이 잡는 삼진형 투수로 상대하는 " + K_percent + "%의 타자를 삼진으로 잡습니다. 9이닝 당 " + K_9 + "개의 삼진을 잡습니다.\n";
        else if(K_percent > 17)
            summary += "리그 평균 수준의 삼진을 잡는 투수로 상대하는 " + K_percent + "%의 타자를 삼진으로 잡습니다. 9이닝 당 " + K_9 + "개의 삼진을 잡습니다.\n";
        else
            summary += "타자를 맞춰잡는 유형의 투수로 상대하는 " + K_percent + "%의 타자를 삼진으로 잡습니다. 9이닝 당 " + K_9 + "개의 삼진을 잡습니다.\n";

        return summary;
    }

    private String vsBatterAnalyze(PitcherZoneStat vsLeft, PitcherZoneStat vsRight){
        Double left_avg = vsLeft.zoneAvg();
        Double right_avg = vsRight.zoneAvg();

        if(left_avg < right_avg)
            return "우타에게 강점을 보입니다.\n";
        else
            return "좌타에게 강점을 보입니다.\n";
    }

    private String count2SAnalyze(PitcherZoneStat count, PitcherZoneStat crisis){
        Double countBall = count.ballZone();
        Double countStrike = count.strikeZone();


        Double ball2S = crisis.ballZone();
        Double strike2S = crisis.strikeZone();


        double ball_rate = Math.round((ball2S / countBall) * 100) / 100.0;
        double strike_rate = Math.round((strike2S / countStrike) * 100) / 100.0;

        if(ball_rate >= 1.2)
            return "2S 이후 유인구를 사용하는 비율이 높습니다. 기존보다" + ball_rate + "배의 볼을 던지고, " + strike_rate + "배의 스트라이크를 던집니다.\n";
        else if(ball_rate >= 1.05)
            return "2S 이후에도 유인구, 승부구의 비율이 비슷합니다. 기존보다 " + ball_rate + "배의 볼을 던지고, " + strike_rate + "배의 스트라이크를 던집니다.\n";
        else
            return "2S 이후에도 적극적으로 스트라이크존에 승부합니다. 기존보다 " + ball_rate + "배의 볼을 던지고 "+ strike_rate + "배의 스트라이크를 던집니다.\n";

    }

    private String count2SZoneAnalyze(Player player){
        PitcherZoneStat count = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "카운트", "구사율");
        PitcherZoneStat crisis = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "결정구", "구사율");

        Double countLeftUpside = count.leftUpside();
        Double countRightUpside = count.rightUpside();
        Double countLeftDownside = count.leftDownside();
        Double countRightDownside = count.rightDownside();

        Double leftUpside2S = crisis.leftUpside();
        Double rightUpside2S = crisis.rightUpside();
        Double leftDownside2S = crisis.leftDownside();
        Double rightDownside2S = crisis.rightDownside();

        Double maxCount = Math.max(
                Math.max(countLeftUpside, countRightUpside),
                Math.max(countLeftDownside, countRightDownside)
        );

        Double max2S = Math.max(
                Math.max(leftDownside2S, leftUpside2S),
                Math.max(rightDownside2S, rightUpside2S)
        );

        String summary = "";
        if(maxCount.equals(countLeftUpside))
            summary = "카운트를 잡을 때 왼쪽 상단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 1);
        else if(maxCount.equals(countRightUpside))
            summary = "카운트를 잡을 때 오른쪽 상단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 2);
        else if(maxCount.equals(countLeftDownside))
            summary = "카운트를 잡을 때 왼쪽 하단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 3);
        else
            summary = "카운트를 잡을 때 오른쪽 하단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 4);

        if(max2S.equals(countLeftUpside))
            summary += "2S 이후 왼쪽 상단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 "+ zonePitchType(player, 1);
        else if(max2S.equals(countRightUpside))
            summary += "2S 이후 오른쪽 상단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 2);
        else if(max2S.equals(countLeftDownside))
            summary += "2S 이후 왼쪽 하단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 3);
        else
            summary += "2S 이후 오른쪽 하단에 많이 던집니다. 약 " + maxCount + "%의 공을 해당 존에 투구하고 " + zonePitchType(player, 4);
        
        return summary;
    }

    private String zonePitchType(Player player, int tag){
        PitcherZoneStat fastball_zone = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "패스트볼", "구사율");
        PitcherZoneStat changeup_zone = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "체인지업", "구사율");
        PitcherZoneStat curve_zone = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "커브", "구사율");
        PitcherZoneStat slider_zone = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "슬라이더", "구사율");

        Double fastball = 0.0;
        Double changeup = 0.0;
        Double curve = 0.0;
        Double slider = 0.0;
        if(tag == 1){
            if(fastball_zone != null) fastball = fastball_zone.leftUpside();
            if(changeup_zone != null) changeup = changeup_zone.leftUpside();
            if(curve_zone != null) curve = curve_zone.leftUpside();
            if(slider_zone != null) slider = slider_zone.leftUpside();
        }else if (tag == 2){
            if(fastball_zone != null) fastball = fastball_zone.rightUpside();
            if(changeup_zone != null) changeup = changeup_zone.rightUpside();
            if(curve_zone != null) curve = curve_zone.rightUpside();
            if(slider_zone != null) slider = slider_zone.rightUpside();
        }else if (tag == 3){
            if(fastball_zone != null) fastball = fastball_zone.leftDownside();
            if(changeup_zone != null) changeup = changeup_zone.leftDownside();
            if(curve_zone != null) curve = curve_zone.leftDownside();
            if(slider_zone != null) slider = slider_zone.leftDownside();
        }else{
            if(fastball_zone != null) fastball = fastball_zone.rightDownside();
            if(changeup_zone != null) changeup = changeup_zone.rightDownside();
            if(curve_zone != null) curve = curve_zone.rightDownside();
            if(slider_zone != null) slider = slider_zone.rightDownside();
        }

        Double max = Math.max(
                Math.max(fastball, changeup),
                Math.max(curve, slider)
        );

        if(max.equals(fastball))
            return "패스트볼 비율이 가장 높습니다.\n";
        else if(max.equals(changeup))
            return "체인지업 비율이 가장 높습니다.\n";
        else if(max.equals(curve))
            return  "커브 비율이 가장 높습니다.\n";
        else
            return "슬라이더 비율이 가장 높습니다.\n";
    }

    private List<PitcherAnalysisDTO.PitcherZoneDTO> crisisZone(Player player){
        List<PitcherZoneStat> fastballZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "패스트볼");
        List<PitcherZoneStat> sliderZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "슬라이더");
        List<PitcherZoneStat> changeupZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "체인지업");
        List<PitcherZoneStat> curveZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "커브");
        PitcherZoneStat countAvg = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "카운트", "타율");
        PitcherZoneStat Avg_2S = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "결정구", "타율");
        PitcherZoneStat vs_right = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "우타", "타율");
        PitcherZoneStat vs_left = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "좌타", "타율");

        PitcherAnalysisDTO.PitcherZoneDTO count_avg_zone = new PitcherAnalysisDTO.PitcherZoneDTO(countAvg);
        count_avg_zone.setTitle("유리한 카운트에서 타율");
        PitcherAnalysisDTO.PitcherZoneDTO avg_2S_zone = new PitcherAnalysisDTO.PitcherZoneDTO(Avg_2S);
        avg_2S_zone.setTitle("불리한 카운트에서 타율");


        List<PitcherAnalysisDTO.PitcherZoneDTO> crisisZoneList = new ArrayList<>();
        crisisZoneList.add(count_avg_zone);
        crisisZoneList.add(avg_2S_zone);
        crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(vs_left));
        crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(vs_right));
        if (fastballZone.size() >= 2) {
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(fastballZone.get(0)));
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(fastballZone.get(1)));
        }
        if (sliderZone.size() >= 2) {
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(sliderZone.get(0)));
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(sliderZone.get(1)));
        }
        if (changeupZone.size() >= 2) {
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(changeupZone.get(0)));
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(changeupZone.get(1)));
        }
        if (curveZone.size() >= 2) {
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(curveZone.get(0)));
            crisisZoneList.add(new PitcherAnalysisDTO.PitcherZoneDTO(curveZone.get(1)));
        }

        return crisisZoneList;
    }
}
