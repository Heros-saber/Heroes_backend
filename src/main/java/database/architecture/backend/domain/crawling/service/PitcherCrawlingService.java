package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.pitcher.PitcherStatsDTO;
import database.architecture.backend.domain.crawling.dto.pitcher.PitcherZoneDTO;
import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.PitcherZoneStat;
import database.architecture.backend.domain.entity.Player;
import database.architecture.backend.domain.entity.Team;
import database.architecture.backend.domain.repository.*;
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
public class PitcherCrawlingService {
    private final PitcherStatRepository pitcherStatRepository;
    private final PitcherZoneStatRepository zoneStatRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerCrawlingService playerCrawlingService;
    private static final Map<String, Integer> positionMap = new HashMap<>();

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
    }

    public Integer parsePosition(String pos) {
        return positionMap.getOrDefault(pos, null);
    }

    public PitcherZoneStat calcAvg(List<PitcherZoneStat> stats) {
        if (stats == null || stats.isEmpty()) {
            return null; // 입력값이 없으면 null 반환
        }

        // 첫 번째 통계 데이터를 기준으로 year, tag, circumstance, player는 그대로 사용
        PitcherZoneStat firstStat = stats.get(0);

        // 각 zone에 대한 평균 계산
        double[] zoneAverages = new double[25];

        for (int i = 0; i < 25; i++) {
            final int index = i + 1;
            zoneAverages[i] = Double.parseDouble(String.format("%.3f", stats.stream()
                    .mapToDouble(stat -> {
                        try {
                            return (Double) PitcherZoneStat.class.getMethod("getZone" + index).invoke(stat);
                        } catch (Exception e) {
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0.0)));
        }

        // 평균값으로 새로운 BatterZoneStat 객체 생성
        return PitcherZoneStat.builder()
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
                .zone25(zoneAverages[024])
                .build();
    }

    @Transactional
    public void postprocessing(Player player){
        List<String> tags = List.of("구사율", "타율");
        List<String> circums = List.of("주자있음", "주자없음", "우언", "좌언", "구종모름", "너클볼", "득점권", "양타");
        List<String> fastBall_column = List.of("투심", "포심", "커터", "싱커");
        List<String> count_column = List.of("초구", "스트라이크 > 볼", "스트라이크 = 볼", "볼 > 스트라이크");
        for (String tag : tags) {
            for (String circum : circums) {
                zoneStatRepository.deletePitcherZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
        }

        for (String tag : tags) {
            List<PitcherZoneStat> fast_list = new ArrayList<>();
            for (String circum : fastBall_column) {
                fast_list.add(zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, circum, tag));
                zoneStatRepository.deletePitcherZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
            PitcherZoneStat pitcherZoneStat = calcAvg(fast_list);
            pitcherZoneStat.updateCircum("패스트볼");
            zoneStatRepository.save(pitcherZoneStat);

            List<PitcherZoneStat> count_list = new ArrayList<>();
            for (String circum : count_column) {
                count_list.add(zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, circum, tag));
                zoneStatRepository.deletePitcherZoneStatByPlayerAndAndCircumstanceAndTag(player, circum, tag);
            }
            PitcherZoneStat countZoneStat = calcAvg(count_list);
            countZoneStat.updateCircum("카운트");
            zoneStatRepository.save(countZoneStat);

            PitcherZoneStat strikeZone = zoneStatRepository.findPitcherZoneStatByPlayerAndCircumstanceAndTag(player, "2S 이후", tag);
            strikeZone.updateCircum("결정구");
            zoneStatRepository.save(strikeZone);
        }
    }

    @Transactional
    public void savePitcher(String name) throws IOException {
        Player checkplayer = playerRepository.findPlayerByPlayerName(name);
        if (checkplayer != null)
            throw new IllegalArgumentException("이미 등록된 선수입니다.");

        int playerId = playerCrawlingService.getPlayerId(name);
        PlayerInfoDTO playerInfo = playerCrawlingService.getPlayerInfo(playerId);
        List<PitcherStatsDTO> pitcherStats = getPitcherStats(playerId);
        List<PitcherZoneDTO> pitcherZoneStats = getPitcherZoneStats(playerId);

        Team team = teamRepository.findTeamByTeamName(playerInfo.getPlayerTeam());
        Player player = playerRepository.save(Player.builder()
                .playerName(name).playerBorn(playerInfo.getPlayerBorn()).playerDraft(playerInfo.getPlayerDraft()).playerPos(parsePosition(playerInfo.getPlayerPos()))
                .playerThrowSide(playerInfo.isPlayerThrowSide()).playerBattingSide(playerInfo.isPlayerBattingSide()).team(team).build());

        for (PitcherStatsDTO pitcherStat : pitcherStats) {
            pitcherStatRepository.save(pitcherStat.toEntity(player));
        }

        for (PitcherZoneDTO pitcherZoneStat : pitcherZoneStats) {
            zoneStatRepository.save(pitcherZoneStat.toEntity(player));
        }

        postprocessing(player);
    }
    public List<PitcherStatsDTO> getPitcherStats(int playerId) throws IOException {
        String url = "https://statiz.sporki.com/player/?m=year&p_no=" + playerId;
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table tbody tr");

        List<PitcherStatsDTO> statsList = new ArrayList<>();
        for (int i = 0; i < rows.size() - 1; i++) {
            Element row = rows.get(i);
            Elements cells = row.select("td");

            if (cells.size() > 0) {
                PitcherStatsDTO stats = new PitcherStatsDTO();
                String year = cells.get(0).text().trim();

                boolean isYearMissing = !year.matches("\\d{4}");
                if (isYearMissing) { // 연도가 아닌 경우
                    year = "-";
                }

                if (!isYearMissing) {
                    stats.setYear(year);
                    stats.setGames(parseInt(cells.get(4).text()));
                    stats.setWin(parseInt(cells.get(10).text()));
                    stats.setLose(parseInt(cells.get(11).text()));
                    stats.setSave(parseInt(cells.get(12).text()));
                    stats.setHold(parseInt(cells.get(13).text()));
                    stats.setIp(parseDouble(cells.get(14).text()));
                    stats.setEra(parseDouble(cells.get(30).text()));
                    stats.setEr(parseInt(cells.get(15).text()));
                    stats.setTbf(parseInt(cells.get(18).text()));
                    stats.setH(parseInt(cells.get(19).text()));
                    stats.setTwoB(parseInt(cells.get(20).text()));
                    stats.setThreeB(parseInt(cells.get(21).text()));
                    stats.setHr(parseInt(cells.get(22).text()));
                    stats.setBb(parseInt(cells.get(23).text()) + parseInt(cells.get(24).text()) + parseInt(cells.get(25).text()));  // BB, HP, IB
                    stats.setSo(parseInt(cells.get(26).text()));
                    stats.setWhip(parseDouble(cells.get(34).text()));
                    stats.setWar(parseDouble(cells.get(35).text()));
                } else {
                    stats.setYear(year);
                    stats.setGames(parseInt(cells.get(3).text()));
                    stats.setWin(parseInt(cells.get(9).text()));
                    stats.setLose(parseInt(cells.get(10).text()));
                    stats.setSave(parseInt(cells.get(11).text()));
                    stats.setHold(parseInt(cells.get(12).text()));
                    stats.setIp(parseDouble(cells.get(13).text()));
                    stats.setEra(parseDouble(cells.get(29).text()));
                    stats.setEr(parseInt(cells.get(14).text()));
                    stats.setTbf(parseInt(cells.get(17).text()));
                    stats.setH(parseInt(cells.get(18).text()));
                    stats.setTwoB(parseInt(cells.get(19).text()));
                    stats.setThreeB(parseInt(cells.get(20).text()));
                    stats.setHr(parseInt(cells.get(21).text()));
                    stats.setBb(parseInt(cells.get(22).text()) + parseInt(cells.get(23).text()) + parseInt(cells.get(24).text()));  // BB, HP, IB
                    stats.setSo(parseInt(cells.get(25).text()));
                    stats.setWhip(parseDouble(cells.get(33).text()));
                    stats.setWar(parseDouble(cells.get(34).text()));
                }
                statsList.add(stats);
            }
        }
        return statsList;
    }

    private double parseDouble(String text) {
        double value = text.isEmpty() ? 0.0f : Float.parseFloat(text);
        return Double.parseDouble(String.format("%.3f", value));
    }

    private int parseInt(String text) {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public List<PitcherZoneDTO> getPitcherZoneStats(int playerId) {
        List<Integer> mapping = List.of(1, 6);
        List<String> mappingLabels = List.of("구사율", "타율");

        List<PitcherZoneDTO> batterZoneDTOList = new ArrayList<>();

        for (int i = 0; i < mapping.size(); i++) {
            Integer key = mapping.get(i);
            String label = mappingLabels.get(i);
            String url = "https://statiz.sporki.com/player/?m=analysis&p_no=" + playerId + "&pos=pitching&year=2024&si1=3&si2=" + key;
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
                        batterZoneDTOList.add(new PitcherZoneDTO(titleText, label, pitchingAverages));

                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("해당 선수를 크롤링할 수 없습니다.");
            }
        }
        return batterZoneDTOList;
    }
}
