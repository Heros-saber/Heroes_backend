package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
