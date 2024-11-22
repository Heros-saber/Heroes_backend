package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.MatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {
}
