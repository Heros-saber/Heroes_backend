package database.architecture.backend.domain.listener;

import database.architecture.backend.domain.entity.BatterStat;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BatterStatListener {

    @PrePersist
    @PreUpdate
    public void validateStats(BatterStat batterStat) {

        if (batterStat.getOps() > 5.0) {
            throw new IllegalArgumentException("OPS cannot exceed 5.0");
        }
        if (batterStat.getSlg() > 4.0) {
            throw new IllegalArgumentException("SLG cannot exceed 4.0");
        }
        if (batterStat.getObp() > 1.0) {
            throw new IllegalArgumentException("OBP cannot exceed 1.0");
        }
        if (batterStat.getAvg() > 1.0) {
            throw new IllegalArgumentException("AVG cannot exceed 1.0");
        }
    }
}
