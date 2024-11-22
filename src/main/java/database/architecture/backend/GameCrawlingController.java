package database.architecture.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GameCrawlingController {

    @GetMapping("/game-crawling")
    public List<GameData> gameCrawling(@RequestParam int year, @RequestParam int month) {
        String url = String.format(
                "https://heroesbaseball.co.kr/games/schedule/getSports2iScheduleList.do?searchYear=%04d&searchMonth=%02d&flag=1",
                year, month);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<GameData> gameDataList = new ArrayList<>();

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
                        gameDataList.add(new GameData(
                                String.format("%04d-%02d-%s", year, month, gday),
                                awayTeam,
                                awayScore,
                                homeScore
                        ));
                    } else if ("키움".equals(awayTeam)) {
                        gameDataList.add(new GameData(
                                String.format("%04d-%02d-%s", year, month, gday),
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

    // 데이터 구조 정의
    public static class GameData {
        private String date;
        private String opponentTeam;
        private int opponentScore;
        private int kiwoomScore;

        public GameData(String date, String opponentTeam, int opponentScore, int kiwoomScore) {
            this.date = date;
            this.opponentTeam = opponentTeam;
            this.opponentScore = opponentScore;
            this.kiwoomScore = kiwoomScore;
        }

        public String getDate() {
            return date;
        }

        public String getOpponentTeam() {
            return opponentTeam;
        }

        public int getOpponentScore() {
            return opponentScore;
        }

        public int getKiwoomScore() {
            return kiwoomScore;
        }

        @Override
        public String toString() {
            return "GameData{" +
                    "date='" + date + '\'' +
                    ", opponentTeam='" + opponentTeam + '\'' +
                    ", opponentScore=" + opponentScore +
                    ", kiwoomScore=" + kiwoomScore +
                    '}';
        }
    }
}
