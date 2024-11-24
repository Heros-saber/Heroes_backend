package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterZoneDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterStatsDTO;
import database.architecture.backend.domain.entity.BatterStat;
import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.entity.Team;
import database.architecture.backend.domain.repository.BatterStatRepository;
import database.architecture.backend.domain.repository.BatterZoneStatRepository;
import database.architecture.backend.domain.repository.PlayerRepository;
import database.architecture.backend.domain.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatterCrawlingService {
    private final BatterStatRepository batterStatRepository;
    private final BatterZoneStatRepository zoneStatRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerCrawlingService playerCrawlingService;
    private static final Map<String, Integer> positionMap = new HashMap<>();
    private static final Map<String, Integer> batterIdList = new HashMap();
    static {
        positionMap.put("P", 1);
        positionMap.put("C", 2);
        positionMap.put("1B", 3);
        positionMap.put("2B", 4);
        positionMap.put("3B", 5);
        positionMap.put("SS", 6);
        positionMap.put("LF", 7);
        positionMap.put("CF", 8);
        positionMap.put("RF", 9);
        positionMap.put("DH", 10);

        batterIdList.put("송성문", 11376);
        batterIdList.put("김혜성", 12905);
        batterIdList.put("최주환", 10182);
        batterIdList.put("도슨", 15652);
        batterIdList.put("이주형", 14591);
        batterIdList.put("김건희", 15472);
        batterIdList.put("김태진", 11244);
        batterIdList.put("박수종", 15132);
        batterIdList.put("원성준", 16136);
        batterIdList.put("이승원", 15476);
        batterIdList.put("김웅빈", 12053);
        batterIdList.put("변상권", 13223);
        batterIdList.put("김병휘", 14577);
    }

    public Integer parsePosition(String pos) {
        return positionMap.getOrDefault(pos, null);
    }

    public BatterZoneStat calcAvg(List<BatterZoneStat> stats) {
        if (stats == null || stats.isEmpty()) {
            return null;
        }

        BatterZoneStat firstStat = stats.get(0);

        double[] zoneAverages = new double[25];

        for (int i = 0; i < 25; i++) {
            final int index = i + 1;
            zoneAverages[i] = Double.parseDouble(String.format("%.3f", stats.stream()
                    .mapToDouble(stat -> {
                        try {
                            return (Double) BatterZoneStat.class.getMethod("getZone" + index).invoke(stat);
                        } catch (Exception e) {
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0.0)));
        }

        // 평균값으로 새로운 BatterZoneStat 객체 생성
        return BatterZoneStat.builder()
                .year(firstStat.getYear()) // year 유지
                .tag(firstStat.getTag()) // tag 유지
                .circumstance(firstStat.getCircumstance()) // circumstance 유지
                .player(firstStat.getPlayer()) // player 유지
                .zone1(zoneAverages[0])
                .zone2(zoneAverages[1])
                .zone3(zoneAverages[2])
                .zone4(zoneAverages[3])
                .zone5(zoneAverages[4])
                .zone6(zoneAverages[5])
                .zone7(zoneAverages[6])
                .zone8(zoneAverages[7])
                .zone9(zoneAverages[8])
                .zone10(zoneAverages[9])
                .zone11(zoneAverages[10])
                .zone12(zoneAverages[11])
                .zone13(zoneAverages[12])
                .zone14(zoneAverages[13])
                .zone15(zoneAverages[14])
                .zone16(zoneAverages[15])
                .zone17(zoneAverages[16])
                .zone18(zoneAverages[17])
                .zone19(zoneAverages[18])
                .zone20(zoneAverages[19])
                .zone21(zoneAverages[20])
                .zone22(zoneAverages[21])
                .zone23(zoneAverages[22])
                .zone24(zoneAverages[23])
                .zone25(zoneAverages[24])
                .build();
    }

    @Transactional
    public void postprocessing(Player player){
        List<String> tags = List.of("스윙율", "컨택율", "타율", "ops");
        List<String> circums = List.of("주자있음", "주자없음", "우언", "좌언", "구종모름", "너클볼", "득점권");
        List<String> fastBall_column = List.of("투심", "포심", "커터", "싱커");
        List<String> count_column = List.of("초구", "스트라이크 > 볼", "스트라이크 = 볼", "볼 > 스트라이크");
        for (String tag : tags) {
            for (String circum : circums) {
                zoneStatRepository.deleteBatterZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
        }

        for (String tag : tags) {
            List<BatterZoneStat> fast_list = new ArrayList<>();
            for (String circum : fastBall_column) {
                BatterZoneStat fastZone = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, circum, tag);
                if(fastZone != null)
                    fast_list.add(fastZone);
                zoneStatRepository.deleteBatterZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
            BatterZoneStat batterZoneStat = calcAvg(fast_list);
            batterZoneStat.updateCircum("패스트볼");
            zoneStatRepository.save(batterZoneStat);

            List<BatterZoneStat> count_list = new ArrayList<>();
            for (String circum : count_column) {
                BatterZoneStat countZone = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, circum, tag);
                if(countZone != null)
                    count_list.add(countZone);
                zoneStatRepository.deleteBatterZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
            BatterZoneStat countZoneStat = calcAvg(count_list);
            countZoneStat.updateCircum("카운트");
            zoneStatRepository.save(countZoneStat);

            BatterZoneStat strikeZone = zoneStatRepository.findBatterZoneStatByPlayerAndCircumstanceAndTag(player, "2S 이후", tag);
            strikeZone.updateCircum("결정구");
            zoneStatRepository.save(strikeZone);
        }
    }

    @Transactional
    public void saveBatter(String name) throws IOException {
        Player checkplayer = playerRepository.findPlayerByPlayerName(name);
        if (checkplayer != null)
            throw new IllegalArgumentException("이미 등록된 선수 입니다.");

        int playerId;
        if(batterIdList.containsKey(name))
            playerId = batterIdList.get(name);
        else
            playerId = playerCrawlingService.getPlayerId(name);
        PlayerInfoDTO playerInfo = playerCrawlingService.getPlayerInfo(playerId);
        List<BatterStatsDTO> batterStats = getBatterStats(playerId);
        List<BatterZoneDTO> batterZoneStats = getBatterZoneStats(playerId);

        Team team = teamRepository.findTeamByTeamName(playerInfo.getPlayerTeam());
        Player player = playerRepository.save(Player.builder()
                .playerName(name).playerBorn(playerInfo.getPlayerBorn()).playerDraft(playerInfo.getPlayerDraft()).playerPos(parsePosition(playerInfo.getPlayerPos()))
                .playerThrowSide(playerInfo.isPlayerThrowSide()).playerBattingSide(playerInfo.isPlayerBattingSide()).team(team).build());

        for (BatterStatsDTO batterStat : batterStats) {
            batterStatRepository.save(batterStat.toEntity(player));
        }

        for (BatterZoneDTO batterZoneStat : batterZoneStats) {
            zoneStatRepository.save(batterZoneStat.toEntity(player));
        }
        postprocessing(player);
    }
    public List<BatterStatsDTO> getBatterStats(int playerId) throws IOException {
        String url = "https://statiz.sporki.com/player/?m=year&p_no=" + playerId;
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table tbody tr");

        List<BatterStatsDTO> statsList = new ArrayList<>();

        for (int i = 0; i < rows.size() - 1; i++) {
            Element row = rows.get(i);
            Elements cells = row.select("td");

            if (cells.size() > 0) {
                BatterStatsDTO stats = new BatterStatsDTO();
                String year = cells.get(0).text().trim();

                if (!year.matches("\\d{4}")) { // 연도가 아닌 경우
                    year = "-";
                }

                boolean isYearMissing = !year.matches("\\d{4}");
                if (isYearMissing) {
                    stats.setYear(year);
                    stats.setHAvg(parseDouble(cells.get(25).text()));
                    stats.setObp(parseDouble(cells.get(26).text()));
                    stats.setSlg(parseDouble(cells.get(27).text()));
                    stats.setOps(parseDouble(cells.get(28).text()));
                    stats.setWrcPlus(parseDouble(cells.get(30).text()));
                    stats.setH(parseInt(cells.get(10).text()));
                    stats.setTwoB(parseInt(cells.get(11).text()));
                    stats.setThreeB(parseInt(cells.get(12).text()));
                    stats.setHr(parseInt(cells.get(13).text()));
                    stats.setRbi(parseInt(cells.get(15).text()));
                    stats.setSb(parseInt(cells.get(16).text()));
                    stats.setBb(parseInt(cells.get(18).text()) + parseInt(cells.get(19).text()) + parseInt(cells.get(20).text()));  // BB, HP, IB
                    stats.setSo(parseInt(cells.get(21).text()));
                    stats.setPa(parseInt(cells.get(6).text()));
                    stats.setAb(parseInt(cells.get(8).text()));
                    stats.setWar(parseDouble(cells.get(31).text()));
                } else {
                    stats.setYear(year);
                    stats.setHAvg(parseDouble(cells.get(26).text()));
                    stats.setObp(parseDouble(cells.get(27).text()));
                    stats.setSlg(parseDouble(cells.get(28).text()));
                    stats.setOps(parseDouble(cells.get(29).text()));
                    stats.setWrcPlus(parseDouble(cells.get(31).text()));
                    stats.setH(parseInt(cells.get(11).text()));
                    stats.setTwoB(parseInt(cells.get(12).text()));
                    stats.setThreeB(parseInt(cells.get(13).text()));
                    stats.setHr(parseInt(cells.get(14).text()));
                    stats.setRbi(parseInt(cells.get(16).text()));
                    stats.setSb(parseInt(cells.get(17).text()));
                    stats.setBb(parseInt(cells.get(19).text()) + parseInt(cells.get(20).text()) + parseInt(cells.get(21).text()));  // BB, HP, IB
                    stats.setSo(parseInt(cells.get(22).text()));
                    stats.setPa(parseInt(cells.get(7).text()));
                    stats.setAb(parseInt(cells.get(9).text()));
                    stats.setWar(parseDouble(cells.get(32).text()));
                }

                statsList.add(stats);
            }
        }
        return statsList;
    }

    private double parseDouble(String text) {
        double value = text.isEmpty() ? 0.0f : Float.parseFloat(text);
        return Double.parseDouble(String.format("%.4f", value));
    }

    private int parseInt(String text) {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public List<BatterZoneDTO> getBatterZoneStats(int playerId) {
        // 매핑
        List<Integer> mapping = List.of(4, 5, 6, 9);
        List<String> mappingLabels = List.of("스윙율", "컨택율", "타율", "ops");

        List<BatterZoneDTO> playerDataDTOList = new ArrayList<>();

        for (int i = 0; i < mapping.size(); i++) {
            Integer key = mapping.get(i);
            String label = mappingLabels.get(i);
            String url = "https://statiz.sporki.com/player/?m=analysis&p_no=" + playerId + "&pos=batting&year=2024&si1=3&si2=" + key;
            try {
                Document doc = Jsoup.connect(url).get();
                List<Double> tablesData = new ArrayList<>();
                var tables = doc.select("div.box_item_3dan");

                for (Element table : tables) {
                    var titles = table.select("div.h_tit");
                    var itemContainers = table.select("div.item_con");

                    for (int j = 0; j < titles.size(); j++) {
                        String titleText = titles.get(j).text();
                        List<Double> pitchingAverages = new ArrayList<>();
                        var items = itemContainers.get(j).select("div.info");

                        for (Element item : items) {
                            String avgText = item.select("strong").text();
                            try {
                                pitchingAverages.add(Double.parseDouble(avgText));
                            } catch (NumberFormatException e) {
                                pitchingAverages.add(null);
                            }
                        }
                        while (pitchingAverages.size() < 25) {
                            pitchingAverages.add(null);
                        }
                        playerDataDTOList.add(new BatterZoneDTO(titleText, label, pitchingAverages));

                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("해당 선수를 크롤링할 수 없습니다.");
            }
        }
        return playerDataDTOList;
    }
}
