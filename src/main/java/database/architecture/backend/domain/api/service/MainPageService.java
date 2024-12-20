package database.architecture.backend.domain.api.service;

import database.architecture.backend.domain.api.dto.MainPageDTO;
import database.architecture.backend.domain.entity.HeroesRecord;
import database.architecture.backend.domain.entity.MatchRecord;
import database.architecture.backend.domain.repository.HeroesRecordRepository;
import database.architecture.backend.domain.repository.MatchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final HeroesRecordRepository heroesRecordRepository;
    private final MatchRecordRepository matchRecordRepository;

    public MainPageDTO.MainPageListDTO getMainPageData() {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        HeroesRecord heroesRecord = heroesRecordRepository.findHeroesRecordByYear(currentYear);
        int rank = heroesRecord.getRanking();
        int win = heroesRecord.getWin();
        int lose = heroesRecord.getLose();

        List<MatchRecord> upcomingGames = matchRecordRepository.findAllByYearAndMonthAndDayAfterOrderByDay(
                currentYear, currentMonth, LocalDate.now().getDayOfMonth()
        );
        LocalDate nextGameDate = upcomingGames.isEmpty() ? null : LocalDate.of(
                upcomingGames.get(0).getYear(),
                upcomingGames.get(0).getMonth(),
                upcomingGames.get(0).getDay()
        );

        return MainPageDTO.MainPageListDTO.builder()
                .rank(rank)
                .win(win)
                .lose(lose)
                .nextGame(nextGameDate == null ? null : nextGameDate.toString())
                .build();
    }

    public List<MainPageDTO.MatchRecordDTO> getMonthlyMatches(int year, int month) {
        List<MatchRecord> matches = matchRecordRepository.findByYearAndMonthOrderByDayAsc(year, month);
        return matches.stream()
                .map(matchRecord -> MainPageDTO.MatchRecordDTO.builder()
                        .date(LocalDate.of(matchRecord.getYear(), matchRecord.getMonth(), matchRecord.getDay()))
                        .kiwoomScore(matchRecord.getHeroesScore())
                        .opponentScore(matchRecord.getOppoScore())
                        .win(matchRecord.getWin())
                        .build()
                ).collect(Collectors.toList());
    }
}
