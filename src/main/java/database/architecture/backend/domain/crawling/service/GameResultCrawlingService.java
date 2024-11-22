package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.game.GameResultDTO;
import database.architecture.backend.domain.crawling.dto.game.TeamRankDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameResultCrawlingService {
    public TeamRankDTO getTeamRank() throws IOException {
        String url = "https://statiz.sporki.com/team/?m=cteam&ct_code=11";
        Document doc = Jsoup.connect(url).get();
        int currentYear = LocalDate.now().getYear();

        // 테이블 본문에서 데이터 추출
        Elements rows = doc.select(".box_cont .table_type03 tbody tr");
        for (Element row : rows) {
            String yearText = row.select("td").get(0).text(); // 연도 추출
            if (yearText.contains(String.valueOf(currentYear))) { // 연도가 일치하면
                int rank = Integer.parseInt(row.select("td div span").text()); // 순위
                int win = Integer.parseInt(row.select("td").get(3).text()); // 승
                int draw = Integer.parseInt(row.select("td").get(4).text()); // 무
                int lose = Integer.parseInt(row.select("td").get(5).text()); // 패
                return new TeamRankDTO(rank, win, lose, draw);
            }
        }
        return null;
    }
    public List<GameResultDTO> gameCrawling(int year, int month) {

        String url = String.format(
                "https://heroesbaseball.co.kr/games/schedule/getSports2iScheduleList.do?searchYear=%04d&searchMonth=%02d&flag=1",
                year, month);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<GameResultDTO> gameDataList = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseData = response.getBody();
            List<Map<String, Object>> gamesList = (List<Map<String, Object>>) responseData.get("scheduleList");

            if (gamesList != null && !gamesList.isEmpty()) {
                for (Map<String, Object> game : gamesList) {
                    String gday = (String) game.get("gday");
                    String homeTeam = (String) game.get("home");
                    String awayTeam = (String) game.get("visit");
                    int homeScore = (int) game.getOrDefault("hscore", 0);
                    int awayScore = (int) game.getOrDefault("vscore", 0);

                    // 키움 팀과 관련된 경기만 필터링
                    if ("키움".equals(homeTeam)) {
                        gameDataList.add(new GameResultDTO(
                                LocalDate.of(year, month, Integer.parseInt(gday)),
                                awayTeam,
                                awayScore,
                                homeScore
                        ));
                    } else if ("키움".equals(awayTeam)) {
                        gameDataList.add(new GameResultDTO(
                                LocalDate.of(year, month, Integer.parseInt(gday)),
                                homeTeam,
                                homeScore,
                                awayScore
                        ));
                    }
                }
            }
        }
        return gameDataList;
    }
}
