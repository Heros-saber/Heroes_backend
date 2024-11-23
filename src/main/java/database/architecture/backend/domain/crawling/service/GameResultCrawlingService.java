package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.game.GameResultDTO;
import database.architecture.backend.domain.crawling.dto.game.TeamRankDTO;
import database.architecture.backend.domain.entity.HeroesRecord;
import database.architecture.backend.domain.entity.MatchRecord;
import database.architecture.backend.domain.entity.Team;
import database.architecture.backend.domain.repository.HeroesRecordRepository;
import database.architecture.backend.domain.repository.MatchRecordRepository;
import database.architecture.backend.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameResultCrawlingService {
    private final HeroesRecordRepository heroesRecordRepository;
    private final MatchRecordRepository matchRepository;
    private final TeamRepository teamRepository;
    @Scheduled(cron = "0 0 0 * * *")
    public void getTeamRank() throws IOException {
        int currentYear = LocalDate.now().getYear();
        HeroesRecord heroesRecord = heroesRecordRepository.findHeroesRecordByYear(currentYear);

        String url = "https://statiz.sporki.com/team/?m=cteam&ct_code=11";
        Document doc = Jsoup.connect(url).get();

        Elements rows = doc.select(".box_cont .table_type03 tbody tr");
        for (Element row : rows) {
            String yearText = row.select("td").get(0).text();
            if (yearText.contains(String.valueOf(currentYear))) {
                int rank = Integer.parseInt(row.select("td div span").text());
                int win = Integer.parseInt(row.select("td").get(3).text());
                int draw = Integer.parseInt(row.select("td").get(4).text());
                int lose = Integer.parseInt(row.select("td").get(5).text());
                if(heroesRecord != null) {
                    heroesRecord.updateHeroesRecord(win, lose, draw, rank);
                    heroesRecordRepository.save(heroesRecord);
                }else {
                    heroesRecordRepository.save(HeroesRecord.builder().win(win).year(currentYear).lose(lose)
                            .draw(draw).ranking(rank).build());
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void gameCrawling(int year, int month) {

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

        List<MatchRecord> recordList = matchRepository.findAllByYearAndMonth(year, month);
        if (recordList.isEmpty()) {
            for (GameResultDTO gameResult : gameDataList) {
                Team team = teamRepository.findTeamByTeamName(gameResult.getOpponentTeam());
                matchRepository.save(gameResult.toEntity(team));
            }
        } else {
            for (int i = 0; i < gameDataList.size(); i++) {
                recordList.get(i).updateMatchRecord(gameDataList.get(i));
                matchRepository.save(recordList.get(i));
            }
        }
    }
}
