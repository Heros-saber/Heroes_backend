package database.architecture.backend.service;

import database.architecture.backend.dto.PlayerInfoDTO;
import database.architecture.backend.dto.PlayerStatsDTO;
import database.architecture.backend.dto.PlayerZoneDTO;
import database.architecture.backend.utils.WebDriverUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlayerService {

    public int getPlayerId(String name) {
        WebDriver driver = WebDriverUtils.createDriver();
        try {
            String url = "https://statiz.sporki.com/player/?m=search&s=" + name;
            driver.get(url);
            String finalUrl = driver.getCurrentUrl();
            Pattern pattern = Pattern.compile("p_no=(\\d+)");
            Matcher matcher = pattern.matcher(finalUrl);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            return -1;
        } finally {
            driver.quit();
        }
    }

    public PlayerInfoDTO getPlayerInfo(int playerId, String playerName) throws IOException {
        String url = "https://statiz.sporki.com/player/?m=playerinfo&p_no=" + playerId;

        // Selenium WebDriver 설정 (헤드리스 모드)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 브라우저 창이 나타나지 않음
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        driver.get(url);

        // HTML 페이지 소스 가져오기
        String pageSource = driver.getPageSource();
        driver.quit(); // WebDriver 종료

        // Jsoup으로 HTML 파싱
        Document doc = Jsoup.parse(pageSource);

        // 생년월일 및 기타 정보 파싱
        Elements details = doc.select("li span");
        Map<String, String> info = new HashMap<>();
        for (Element detail : details) {
            String label = detail.text().replace(":", "").trim();
            String value = detail.parent().ownText().trim();
            info.put(label, value);
        }

        // 포지션 및 타격, 투구 정보 파싱
        String position = "";
        String battingThrow = "";
        try {
            position = doc.select("div.con span").get(1).text();
            battingThrow = doc.select("div.con span").get(2).text();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("포지션 또는 타격/투구 정보를 가져올 수 없습니다.");
        }

        boolean battingSide = battingThrow.contains("우타");
        boolean throwingSide = battingThrow.contains("우투");

        return new PlayerInfoDTO(
                playerName,
                info.getOrDefault("생년월일", "정보 없음"),
                info.getOrDefault("신인지명", "정보 없음"),
                position,
                battingSide,
                throwingSide
        );
    }

    public List<PlayerStatsDTO> getPlayerStats(int playerId, String playerName) throws IOException {
        String url = "https://statiz.sporki.com/player/?m=year&p_no=" + playerId;
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table tbody tr");

        List<PlayerStatsDTO> statsList = new ArrayList<>();
        for (int i = 0; i < rows.size() - 1; i++) { // 마지막 행을 제외
            Element row = rows.get(i);
            Elements cells = row.select("td");

            if (cells.size() > 0) {
                PlayerStatsDTO stats = new PlayerStatsDTO();
                String year = cells.get(0).text().trim();

                // cells.get(0)이 연도가 아닌 경우, index_mapping을 조정
                if (!year.matches("\\d{4}")) { // 연도가 아닌 경우
                    year = "-";
                }

                // 연도가 없는 경우, 인덱스를 조정
                boolean isYearMissing = !year.matches("\\d{4}");
                if (isYearMissing) {
                    // "basic_mapping"에서 인덱스를 하나씩 밀어서 맞춤
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
                    // 정상적인 경우, 기본 index_mapping을 사용 하여 데이터를 매핑
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

    public List<PlayerZoneDTO> playerZoneCrawlingAndPreprocessing(String playerId, String name) {
        // 매핑
        String[] mapping = {"4", "5", "6", "9"};
        String[] mappingLabels = {"스윙율", "컨택율", "타율", "ops"};

        List<PlayerZoneDTO> playerDataDTOList = new ArrayList<>();

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
                                battingAverages.add(Double.parseDouble(avgText));  // 문자열을 Double로 변환
                            } catch (NumberFormatException e) {
                                battingAverages.add(null);  // 변환 불가 시 null
                            }
                        }

                        while (battingAverages.size() < 25) {
                            battingAverages.add(null);  // 25개 미만이면 null로 채움
                        }

                        // Double[]로 변환하여 저장
                        Double[] row = new Double[battingAverages.size() + 1];
                        label = titleText;  // 첫 번째 열은 문자열로 저장
                        for (int k = 0; k < battingAverages.size(); k++) {
                            row[k] = battingAverages.get(k);
                        }

                        tablesData.add(row);
                    }
                }

                // DTO로 반환
                PlayerZoneDTO playerDataDTO = new PlayerZoneDTO(label, label, tablesData);
                playerDataDTOList.add(playerDataDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return playerDataDTOList;  // 크롤링한 데이터를 DTO 리스트로 반환
    }
}


