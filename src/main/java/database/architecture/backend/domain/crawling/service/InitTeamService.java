package database.architecture.backend.domain.crawling.service;

import database.architecture.backend.domain.entity.Team;
import database.architecture.backend.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitTeamService implements ApplicationListener<ContextRefreshedEvent> {
    private final TeamRepository teamRepository;
    private final GameResultCrawlingService service;
    private final BatterCrawlingService batterCrawlingService;
    private final PitcherCrawlingService pitcherCrawlingService;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<String> teamName = List.of("키움", "KIA", "삼성", "NC", "롯데", "KT", "두산", "SSG", "한화", "LG");
        List<LocalDate> teamFounded = List.of(LocalDate.parse("2008-03-24"), LocalDate.parse("1982-01-30"),
                LocalDate.parse("1982-02-03"), LocalDate.parse("2011-03-31"), LocalDate.parse("1982-02-12"),
                LocalDate.parse("2013-01-17"), LocalDate.parse("1982-01-15"), LocalDate.parse("2000-03-31"),
                LocalDate.parse("1986-03-08"), LocalDate.parse("1982-01-26"));
        List<String> teamHometown = List.of("서울", "광주", "대구", "창원", "부산", "수원", "서울", "인천", "대전", "서울");

        for(int i = 0; i< teamName.size(); i++){
            if(!teamRepository.existsTeamByTeamName(teamName.get(i)))
                teamRepository.save(Team.builder().teamName(teamName.get(i)).
                                teamFounded(teamFounded.get(i)).teamHometown(teamHometown.get(i)).build());
        }

        service.getTeamRank();
        for(int i = 3; i<=10; i++){
            service.gameCrawling(LocalDate.now().getYear(), i);
        }

        try {
            batterCrawlingService.saveBatter("송성문");
            batterCrawlingService.saveBatter("김혜성");
            batterCrawlingService.saveBatter("도슨");
            batterCrawlingService.saveBatter("변상권");
            batterCrawlingService.saveBatter("이주형");

            pitcherCrawlingService.savePitcher("후라도");
            pitcherCrawlingService.savePitcher("헤이수스");
            pitcherCrawlingService.savePitcher("하영민");
            pitcherCrawlingService.savePitcher("주승우");
            pitcherCrawlingService.savePitcher("조상우");
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
