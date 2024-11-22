package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterZoneDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterStatsDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BatterCrawlingService {
    private final WebDriver webDriver;

    /**
     * 선수 이름으로 스탯티즈의 id를 찾는 함수.
     * 동명이인일 경우는 아직 지원 안함
     * @return 스탯티즈 Id;
     */
    public int getPlayerId(String name) {
        String url = "https://statiz.sporki.com/player/?m=search&s=" + name;
        webDriver.get(url);
        String finalUrl = webDriver.getCurrentUrl();
        Pattern pattern = Pattern.compile("p_no=(\\d+)");
        Matcher matcher = pattern.matcher(finalUrl);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        
        // 찾지 못한 경우 오류 날림
        throw new IllegalArgumentException("선수 정보를 찾을 수 없습니다.");
    }

    /**
     * 선수의 이름, id를 가지고 선수 정보를 크롤링해옴.
     * @return 선수 정보
     */
    public PlayerInfoDTO getPlayerInfo(int playerId){
        String url = "https://statiz.sporki.com/player/?m=playerinfo&p_no=" + playerId;

        webDriver.get(url);
        String pageSource = webDriver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        Elements details = doc.select("li span");
        Map<String, String> info = new HashMap<>();
        for (Element detail : details) {
            String label = detail.text().replace(":", "").trim();
            String value = detail.parent().ownText().trim();
            info.put(label, value);
        }

        String position = doc.select("div.con span").get(1).text();
        String battingThrow = doc.select("div.con span").get(2).text();
        boolean battingSide = battingThrow.contains("우타");
        boolean throwingSide = battingThrow.contains("우투");

        return new PlayerInfoDTO(
                info.getOrDefault("생년월일", "정보 없음"),
                info.getOrDefault("신인지명", "정보 없음"),
                position,
                battingSide,
                throwingSide
        );
    }

    /**
     * 선수의 연도별 성적을 크롤링해오는 함수
     * 하지만 투타 (장재영) 섞인 선수는 불가
     */
    public List<BatterStatsDTO> getPlayerStats(int playerId) throws IOException {
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
                    stats.setHAvg(parseFloat(cells.get(25).text()));
                    stats.setObp(parseFloat(cells.get(26).text()));
                    stats.setSlg(parseFloat(cells.get(27).text()));
                    stats.setOps(parseFloat(cells.get(28).text()));
                    stats.setWrcPlus(parseFloat(cells.get(30).text()));
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
                    stats.setWar(parseFloat(cells.get(31).text()));
                } else {
                    stats.setYear(year);
                    stats.setHAvg(parseFloat(cells.get(26).text()));
                    stats.setObp(parseFloat(cells.get(27).text()));
                    stats.setSlg(parseFloat(cells.get(28).text()));
                    stats.setOps(parseFloat(cells.get(29).text()));
                    stats.setWrcPlus(parseFloat(cells.get(31).text()));
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
                    stats.setWar(parseFloat(cells.get(32).text()));
                }

                statsList.add(stats);
            }
        }
        return statsList;
    }

    private float parseFloat(String text) {
        return text.isEmpty() ? 0.0f : Float.parseFloat(text);
    }

    private int parseInt(String text) {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    /**
     * 선수들의 히팅존 별 성적을 크롤링 해오는 함수
     */
    public List<BatterZoneDTO> getBatterZoneStats(int playerId) {
        // 매핑
        String[] mapping = {"4", "5", "6", "9"};
        String[] mappingLabels = {"스윙율", "컨택율", "타율", "ops"};

        List<BatterZoneDTO> playerDataDTOList = new ArrayList<>();

        for (int i = 0; i < mapping.length; i++) {
            String key = mapping[i];
            String label = mappingLabels[i];
            String url = "https://statiz.sporki.com/player/?m=analysis&p_no=" + playerId + "&pos=batting&year=2024&si1=3&si2=" + key;

            try {
                Document doc = Jsoup.connect(url).get(); // Jsoup을 통해 HTML 가져오기
                List<Double[]> tablesData = new ArrayList<>();
                var tables = doc.select("div.box_item_3dan"); // 3단 테이블 찾기

                for (Element table : tables) {
                    var titles = table.select("div.h_tit");
                    var itemContainers = table.select("div.item_con");

                    for (int j = 0; j < titles.size(); j++) {
                        String titleText = titles.get(j).text();
                        List<Double> battingAverages = new ArrayList<>();
                        var items = itemContainers.get(j).select("div.info");

                        for (Element item : items) {
                            String avgText = item.select("strong").text();
                            try {
                                battingAverages.add(Double.parseDouble(avgText));
                            } catch (NumberFormatException e) {
                                battingAverages.add(null);
                            }
                        }

                        while (battingAverages.size() < 25) {
                            battingAverages.add(null);
                        }

                        Double[] row = new Double[battingAverages.size() + 1];
                        label = titleText;
                        for (int k = 0; k < battingAverages.size(); k++) {
                            row[k] = battingAverages.get(k);
                        }

                        tablesData.add(row);
                    }
                }

                BatterZoneDTO playerDataDTO = new BatterZoneDTO(label, label, tablesData);
                playerDataDTOList.add(playerDataDTO);
            } catch (IOException e) {
                throw new IllegalArgumentException("선수 타격 정보를 찾을 수 없습니다.");
            }
        }

        return playerDataDTOList;
    }
}


