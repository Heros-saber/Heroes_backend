package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.pitcher.PitcherStatsDTO;
import database.architecture.backend.domain.crawling.dto.pitcher.PitcherZoneDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PitcherCrawlingService {

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
                        batterZoneDTOList.add(new PitcherZoneDTO(label, titleText, pitchingAverages));

                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("해당 선수를 크롤링할 수 없습니다.");
            }
        }
        return batterZoneDTOList;
    }
}
