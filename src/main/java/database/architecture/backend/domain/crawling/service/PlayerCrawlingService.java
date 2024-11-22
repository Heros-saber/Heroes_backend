package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PlayerCrawlingService {
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

}
