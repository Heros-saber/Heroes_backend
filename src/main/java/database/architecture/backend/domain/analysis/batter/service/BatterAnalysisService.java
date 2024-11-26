package database.architecture.backend.domain.analysis.batter.service;

import database.architecture.backend.domain.analysis.batter.dto.BatterAnalysisDTO;
import database.architecture.backend.domain.entity.BatterStat;
import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.repository.BatterStatRepository;
import database.architecture.backend.domain.repository.BatterZoneStatRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatterAnalysisService {
    private final BatterZoneStatRepository zoneStatRepository;
    private final BatterStatRepository statRepository;
    private final PlayerRepository playerRepository;

    public BatterAnalysisDTO.BatterAnalyzeResponse analyzeBatter(String name){
        Player player = playerRepository.findPlayerByPlayerName(name);
        String oneLineAnalysis = one_line_analysis(player);
        String recommended = crisisAnalyze(player).split(" ")[3];
        recommended = recommended.substring(0, recommended.length() - 1);
        List<BatterAnalysisDTO.BatterZoneDTO> zoneStat = crisisZone(player, recommended);

        return BatterAnalysisDTO.BatterAnalyzeResponse.builder().one_line_analysis(oneLineAnalysis).zoneStatDTO(zoneStat).build();
    }
    public String one_line_analysis(Player player){
        BatterStat batterStat = statRepository.findBatterStatByPlayerAndYear(player, "2024");
        BatterZoneStat count = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "카운트", "ops");
        BatterZoneStat finishing = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "결정구", "ops");
        BatterZoneStat vs_right = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "우투", "ops");
        BatterZoneStat vs_left = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "좌투", "ops");

        return overallAnalyze(batterStat) + "\n" + vsPitcherAnalyze(vs_right, vs_left) + "\n" +
                count2SAnalyze(count, finishing) + "\n" + crisisAnalyze(player);
    }

    private String vsPitcherAnalyze(BatterZoneStat vs_right, BatterZoneStat vs_left){
        Double vsRightAvg = vs_right.zoneAvg();
        Double vsLeftAvg = vs_left.zoneAvg();

        if(vsLeftAvg < vsRightAvg)
            return "우투에게 강점을 보입니다.";
        else
            return "좌투에게 강점을 보입니다.";
    }
    private String overallAnalyze(BatterStat stat){
        Double avg = stat.getAvg();
        Double slg = stat.getSlg();
        Double wrcPlus = stat.getWrcPlus();

        String wrc_analysis;
        if(wrcPlus > 160)
            wrc_analysis = "KBO 역사에서 손 꼽히는 성적의 타자입니다.\n";
        else if(wrcPlus > 140)
            wrc_analysis = "리그 최정상급 타자입니다.\n";
        else if(wrcPlus > 115)
            wrc_analysis = "리그 상위급 타자입니다.\n";
        else if(wrcPlus > 100)
            wrc_analysis = "리그 평균 타자입니다.\n";
        else if(wrcPlus > 80)
            wrc_analysis = "리그 평균 이하 타자입니다.\n";
        else
            wrc_analysis = "리그 최하위급 타자입니다.\n";

        String batting_analysis = "";
        if(slg > 0.5 && avg > 0.3)
            batting_analysis = "좋은 컨택과 장타율을 고루 갖춘 완성형 타자입니다.";
        else if(slg > 0.5 && avg > 0.25)
            batting_analysis = "적당한 컨택과 장타력을 가진 중장거리 타자입니다.";
        else if(slg > 0.5 && avg < 0.25)
            batting_analysis = "컨택이 좋지 않지만 강한 장타력을 가진 거포형 타자입니다.";
        else if(slg > 0.4 && avg > 0.3)
            batting_analysis = "적당한 장타율과 좋은 컨택을 가진 타자입니다.";
        else if(slg > 0.4 && avg > 0.25)
            batting_analysis = "리그 평균의 컨택과 적당한 장타율을 가진 타자입니다.";
        else if(slg > 0.4 && avg < 0.25)
            batting_analysis = "컨택이 좋지 않은 중장거리 타자입니다.";
        else if(slg < 0.4 && avg > 0.3)
            batting_analysis = "장타력이 부족하지만 꾸준한 안타를 만들어내는 교타자입니다.";
        else if(slg < 0.4 && avg > 0.25)
            batting_analysis = "리그 평균의 컨택과 리그 하위의 장타력을 가진 타자입니다.";
        else if(slg < 0.4 && avg < 0.25)
            batting_analysis = "컨택과 장타력이 모두 리그 하위권인 타자입니다.";

        return wrc_analysis + batting_analysis;
    }
    private String count2SAnalyze(BatterZoneStat count, BatterZoneStat finishing){
        Double count_Ops = count.zoneAvg();
        Double finishing_Ops = finishing.zoneAvg();

        if(finishing_Ops > count_Ops * 0.95)
            return "카운트가 몰려도 흔들리지 않습니다. 급하게 승부하지 않는 것이 좋습니다.";
        else
            return "카운트가 몰리면 매우 흔들립니다. 카운트를 빨리 잡는것이 유효하여 빠른 숭부하는 것을 추천합니다.";
    }

    private String crisisAnalyze(Player player){
        BatterZoneStat overall = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "전체", "ops");

        Double leftDownsideOps = overall.leftDownside();
        Double leftUpsideOps = overall.leftUpside();
        Double rightDownsideOps = overall.rightDownside();
        Double rightUpsideOps = overall.rightUpside();

        Double min = Math.min(
                Math.min(leftDownsideOps, leftUpsideOps),
                Math.min(rightDownsideOps, rightUpsideOps)
        );

        String weakSpot = "";
        String weakBall = "";
        if (!player.getPlayerBattingSide()) {
            if (min.equals(leftUpsideOps)) {
                weakSpot = "몸쪽 위가 약점이고 ";
                weakBall = crisisBallRecommend(player, 1);
            } else if (min.equals(leftDownsideOps)) {
                weakSpot = "몸쪽 아래가 약점이고 ";
                weakBall = crisisBallRecommend(player, 3);
            } else if (min.equals(rightUpsideOps)) {
                weakSpot = "바깥쪽 위가 약점이고 ";
                weakBall = crisisBallRecommend(player, 2);
            } else {
                weakSpot = "바깥쪽 아래가 약점이고 ";
                weakBall = crisisBallRecommend(player, 4);
            }
        } else {
            if (min.equals(leftUpsideOps)) {
                weakSpot = "바깥쪽 위가 약점이고 ";
                weakBall = crisisBallRecommend(player, 1);
            } else if (min.equals(leftDownsideOps)) {
                weakSpot = "바깥쪽 아래가 약점이고 ";
                weakBall = crisisBallRecommend(player ,3);
            } else if (min.equals(rightUpsideOps)) {
                weakSpot = "몸쪽 위가 약점이고 ";
                weakBall = crisisBallRecommend(player, 2);
            } else {
                weakSpot = "몸쪽 아래가 약점이고 ";
                weakBall = crisisBallRecommend(player, 4);
            }
        }

        return weakSpot + weakBall;
    }

    // 1- 스윙율) + 스윙율 * (1-컨택율)
    private String crisisBallRecommend(Player player, int zoneCode){
        Double fastBallOps;
        Double sliderOps;
        Double changeupOps;
        Double curveOps;

        BatterZoneStat fastball = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "패스트볼", "ops");
        BatterZoneStat slider = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "슬라이더", "ops");
        BatterZoneStat changeup = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "체인지업", "ops");
        BatterZoneStat curve = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "커브", "ops");

        Double fastball_out;
        Double slider_out;
        Double changeup_out;
        Double curve_out;

        BatterZoneStat fastSwing = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "패스트볼", "스윙율");
        BatterZoneStat sliderSwing = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "슬라이더", "스윙율");
        BatterZoneStat changeupSwing = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "체인지업", "스윙율");
        BatterZoneStat curveSwing = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "커브", "스윙율");

        BatterZoneStat fastContact = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "패스트볼", "컨택율");
        BatterZoneStat sliderContact = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "슬라이더", "컨택율");
        BatterZoneStat changeupContact = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "체인지업", "컨택율");
        BatterZoneStat curveContact = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "커브", "컨택율");

        BatterZoneStat fastAvg = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "패스트볼", "타율");
        BatterZoneStat sliderAvg = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "슬라이더", "타율");
        BatterZoneStat changeupAvg = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "체인지업", "타율");
        BatterZoneStat curveAvg = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "커브", "타율");

        if(zoneCode == 1){
            fastBallOps = fastball.leftUpside();
            sliderOps = slider.leftUpside();
            changeupOps = changeup.leftUpside();
            curveOps = curve.leftUpside();

            Double fast_swing = fastSwing.leftUpside() / 100;
            Double fast_contact = fastContact.leftUpside() / 100;
            Double fast_avg = fastAvg.leftUpside();
            fastball_out = 1 - fast_swing + fast_swing * (1 - fast_contact) + fast_swing * fast_contact * fast_avg;
            fastball_out = Math.round(fastball_out * 100000.0) / 1000.0;

            Double slider_swing = sliderSwing.leftUpside() / 100;
            Double slider_contact = sliderContact.leftUpside() / 100;
            Double slider_avg = sliderAvg.leftUpside();
            slider_out = 1 - slider_swing + slider_swing * (1 - slider_contact) + slider_swing * slider_contact * slider_avg;
            slider_out = Math.round(slider_out * 100000.0) / 1000.0;

            Double changeup_swing = changeupSwing.leftUpside() / 100;
            Double changeup_contact = changeupContact.leftUpside() / 100;
            Double changeup_avg = changeupAvg.leftUpside();
            changeup_out = 1 - changeup_swing + changeup_swing * (1 - changeup_contact) + changeup_swing * changeup_contact * changeup_avg;
            changeup_out = Math.round(changeup_out * 100000.0) / 1000.0;

            Double curve_swing = curveSwing.leftUpside() / 100;
            Double curve_contact = curveContact.leftUpside() / 100;
            Double curve_avg = curveAvg.leftUpside();
            curve_out = 1 - curve_swing + curve_swing * (1 - curve_contact) + curve_swing * curve_contact * curve_avg;
            curve_out = Math.round(curve_out * 100000.0) / 1000.0;

        }else if(zoneCode == 2){
            fastBallOps = fastball.rightUpside();
            sliderOps = slider.rightUpside();
            changeupOps = changeup.rightUpside();
            curveOps = curve.rightUpside();

            Double fast_swing = fastSwing.rightUpside() / 100;
            Double fast_contact = fastContact.rightUpside() / 100;
            Double fast_avg = fastAvg.rightUpside();
            fastball_out = 1 - fast_swing + fast_swing * (1 - fast_contact) + fast_swing * fast_contact * fast_avg;
            fastball_out = Math.round(fastball_out * 100000.0) / 1000.0;

            Double slider_swing = sliderSwing.rightUpside() / 100;
            Double slider_contact = sliderContact.rightUpside() / 100;
            Double slider_avg = sliderAvg.rightUpside();
            slider_out = 1 - slider_swing + slider_swing * (1 - slider_contact) + slider_swing * slider_contact * slider_avg;
            slider_out = Math.round(slider_out * 100000.0) / 1000.0;

            Double changeup_swing = changeupSwing.rightUpside() / 100;
            Double changeup_contact = changeupContact.rightUpside() / 100;
            Double changeup_avg = changeupAvg.rightUpside();
            changeup_out = 1 - changeup_swing + changeup_swing * (1 - changeup_contact) + changeup_swing * changeup_contact * changeup_avg;
            changeup_out = Math.round(changeup_out * 100000.0) / 1000.0;

            Double curve_swing = curveSwing.rightUpside() / 100;
            Double curve_contact = curveContact.rightUpside() / 100;
            Double curve_avg = curveAvg.rightUpside();
            curve_out = 1 - curve_swing + curve_swing * (1 - curve_contact) + curve_swing * curve_contact * curve_avg;
            curve_out = Math.round(curve_out * 100000.0) / 1000.0;

        }else if(zoneCode == 3){
            fastBallOps = fastball.leftDownside();
            sliderOps = slider.leftDownside();
            changeupOps = changeup.leftDownside();
            curveOps = curve.leftDownside();

            Double fast_swing = fastSwing.leftDownside() / 100;
            Double fast_contact = fastContact.leftDownside() / 100;
            Double fast_avg = fastAvg.leftDownside();
            fastball_out = 1 - fast_swing + fast_swing * (1 - fast_contact) + fast_swing * fast_contact * fast_avg;
            fastball_out = Math.round(fastball_out * 100000.0) / 1000.0;

            Double slider_swing = sliderSwing.leftDownside() / 100;
            Double slider_contact = sliderContact.leftDownside() / 100;
            Double slider_avg = sliderAvg.leftDownside();
            slider_out = 1 - slider_swing + slider_swing * (1 - slider_contact) + slider_swing * slider_contact * slider_avg;
            slider_out = Math.round(slider_out * 100000.0) / 1000.0;

            Double changeup_swing = changeupSwing.leftDownside() / 100;
            Double changeup_contact = changeupContact.leftDownside() / 100;
            Double changeup_avg = changeupAvg.leftDownside();
            changeup_out = 1 - changeup_swing + changeup_swing * (1 - changeup_contact) + changeup_swing * changeup_contact * changeup_avg;
            changeup_out = Math.round(changeup_out * 100000.0) / 1000.0;

            Double curve_swing = curveSwing.leftDownside() / 100;
            Double curve_contact = curveContact.leftDownside() / 100;
            Double curve_avg = curveAvg.leftDownside();
            curve_out = 1 - curve_swing + curve_swing * (1 - curve_contact) + curve_swing * curve_contact * curve_avg;
            curve_out = Math.round(curve_out * 100000.0) / 1000.0;

        }else {
            fastBallOps = fastball.rightDownside();
            sliderOps = slider.rightDownside();
            changeupOps = changeup.rightDownside();
            curveOps = curve.rightDownside();

            Double fast_swing = fastSwing.rightDownside() / 100;
            Double fast_contact = fastContact.rightDownside() / 100;
            Double fast_avg = fastAvg.rightDownside();
            fastball_out = 1 - fast_swing + fast_swing * (1 - fast_contact) + fast_swing * fast_contact * fast_avg;
            fastball_out = Math.round(fastball_out * 100000.0) / 1000.0;

            Double slider_swing = sliderSwing.rightDownside() / 100;
            Double slider_contact = sliderContact.rightDownside() / 100;
            Double slider_avg = sliderAvg.rightDownside();
            slider_out = 1 - slider_swing + slider_swing * (1 - slider_contact) + slider_swing * slider_contact * slider_avg;
            slider_out = Math.round(slider_out * 100000.0) / 1000.0;

            Double changeup_swing = changeupSwing.rightDownside() / 100;
            Double changeup_contact = changeupContact.rightDownside() / 100;
            Double changeup_avg = changeupAvg.rightDownside();
            changeup_out = 1 - changeup_swing + changeup_swing * (1 - changeup_contact) + changeup_swing * changeup_contact * changeup_avg;
            changeup_out = Math.round(changeup_out * 100000.0) / 1000.0;

            Double curve_swing = curveSwing.rightDownside() / 100;
            Double curve_contact = curveContact.rightDownside() / 100;
            Double curve_avg = curveAvg.rightDownside();
            curve_out = 1 - curve_swing + curve_swing * (1 - curve_contact) + curve_swing * curve_contact * curve_avg;
            curve_out = Math.round(curve_out * 100000.0) / 1000.0;
        }

        Double min = Math.min(
                Math.min(fastBallOps, sliderOps),
                Math.min(changeupOps, curveOps)
        );

        if (min.equals(fastBallOps)) {
            return "패스트볼에 약합니다. 결정구로 던질 경우 아웃을 잡을 확률은 " + fastball_out + "% 입니다.";
        } else if (min.equals(sliderOps)) {
            return "슬라이더에 약합니다. 결정구로 던질 경우 아웃을 잡을 확률은 " + slider_out + "% 입니다.";
        } else if (min.equals(changeupOps)) {
            return "체인지업에 약합니다. 결정구로 던질 경우 아웃을 잡을 확률은 " + changeup_out + "% 입니다.";
        } else {
            return "커브에 약합니다. 결정구로 던질 경우 아웃을 잡을 확률은 " + curve_out + "% 입니다.";
        }
    }

    public List<BatterAnalysisDTO.BatterZoneDTO> crisisZone(Player player, String crisis){
        List<BatterZoneStat> fastballZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "패스트볼");
        List<BatterZoneStat> sliderZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "슬라이더");
        List<BatterZoneStat> changeupZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "체인지업");
        List<BatterZoneStat> curveZone = zoneStatRepository.findAllByPlayerAndCircumstance(player, "커브");
        BatterZoneStat countAvg = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "카운트", "타율");
        BatterZoneStat Avg_2S = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "결정구", "타율");
        BatterZoneStat vs_right = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "우투", "타율");
        BatterZoneStat vs_left = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "좌투", "타율");

        BatterAnalysisDTO.BatterZoneDTO count_avg_zone = new BatterAnalysisDTO.BatterZoneDTO(countAvg);
        count_avg_zone.setTitle("유리한 카운트에서 타율");
        BatterAnalysisDTO.BatterZoneDTO avg_2S_zone = new BatterAnalysisDTO.BatterZoneDTO(Avg_2S);
        avg_2S_zone.setTitle("불리한 카운트에서 타율");

        BatterZoneStat crisisSwing = null;
        BatterZoneStat crisisContact = null;
        BatterZoneStat crisisAvg = null;

        if ("패스트볼".equals(crisis)) {
            crisisSwing = fastballZone.get(0);
            crisisContact = fastballZone.get(1);
            crisisAvg = fastballZone.get(2);
        } else if ("슬라이더".equals(crisis)) {
            crisisSwing = sliderZone.get(0);
            crisisContact = sliderZone.get(1);
            crisisAvg = sliderZone.get(2);
        } else if ("체인지업".equals(crisis)) {
            crisisSwing = changeupZone.get(0);
            crisisContact = changeupZone.get(1);
            crisisAvg = changeupZone.get(2);
        } else if ("커브".equals(crisis)) {
            crisisSwing = curveZone.get(0);
            crisisContact = curveZone.get(1);
            crisisAvg = curveZone.get(2);
        }

        List<BatterAnalysisDTO.BatterZoneDTO> crisisZoneList = new ArrayList<>();
        crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(crisisSwing, crisisContact));
        crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(crisisAvg));
        crisisZoneList.add(count_avg_zone);
        crisisZoneList.add(avg_2S_zone);
        crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(vs_left));
        crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(vs_right));

        if (!"패스트볼".equals(crisis)) {
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(fastballZone.get(0), fastballZone.get(1)));
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(fastballZone.get(2)));
        }
        if (!"슬라이더".equals(crisis)) {
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(sliderZone.get(0), sliderZone.get(1)));
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(sliderZone.get(2)));
        }
        if (!"체인지업".equals(crisis)) {
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(changeupZone.get(0), changeupZone.get(1)));
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(changeupZone.get(2)));
        }
        if (!"커브".equals(crisis)) {
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(curveZone.get(0), curveZone.get(1)));
            crisisZoneList.add(new BatterAnalysisDTO.BatterZoneDTO(curveZone.get(2)));
        }

        return crisisZoneList;
    }
}
