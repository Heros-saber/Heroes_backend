package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PlayerCrawlingService {
    private final WebDriver webDriver;

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
        String playerTeam = doc.select("div.con span").get(0).text();
        String position = doc.select("div.con span").get(1).text();
        String battingThrow = doc.select("div.con span").get(2).text();
        boolean battingSide = battingThrow.contains("우타");
        boolean throwingSide = battingThrow.contains("우투");

        return new PlayerInfoDTO(
                playerTeam,
                parseLocalDate(info.getOrDefault("생년월일", "정보 없음")),
                info.getOrDefault("신인지명", "정보 없음"),
                position,
                battingSide,
                throwingSide
        );
    }

    private LocalDate parseLocalDate(String born){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return LocalDate.parse(born, formatter);
    }
}
