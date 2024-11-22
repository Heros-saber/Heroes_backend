package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.crawling.dto.PlayerInfoDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterZoneDTO;
import database.architecture.backend.domain.crawling.dto.batter.BatterStatsDTO;
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
public class BatterCrawlingService {

    public PlayerInfoDTO saveBatter(PlayerInfoDTO playerInfo, List<BatterStatsDTO> batterStats, List<BatterZoneDTO> batterZoneStats){
        return null;
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
                        playerDataDTOList.add(new BatterZoneDTO(label, titleText, pitchingAverages));

                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("해당 선수를 크롤링할 수 없습니다.");
            }
        }
        return playerDataDTOList;
    }
}
