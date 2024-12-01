package database.architecture.backend.domain.trigger;

import database.architecture.backend.domain.entity.BatterStat;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BatterStatTrigger {

    @PrePersist
    @PreUpdate
    public void validateStats(BatterStat batterStat) {
        if (batterStat.getSlg() > 4.0) {
            int totalBases = batterStat.getHit() + 2 * batterStat.getH_double() +
                    3 * batterStat.getTriple() + 4 * batterStat.getHr();
            double slg = totalBases / (batterStat.getAb() * 1.0);
            BigDecimal roundedSlg = new BigDecimal(slg).setScale(3, RoundingMode.HALF_UP);
            batterStat.setSlg(roundedSlg.doubleValue());

            if (roundedSlg.doubleValue() > 4.0) {
                throw new IllegalArgumentException("SLG cannot exceed 4.0");
            }
        }

        if (batterStat.getObp() > 1.0) {
            int plateAppearances = batterStat.getAb() + batterStat.getBb();
            double obp = (batterStat.getHit() + batterStat.getBb()) / (plateAppearances * 1.0);
            BigDecimal roundedObp = new BigDecimal(obp).setScale(3, RoundingMode.HALF_UP);
            batterStat.setObp(roundedObp.doubleValue());

            if (roundedObp.doubleValue() > 1.0) {
                throw new IllegalArgumentException("OBP cannot exceed 1.0");
            }
        }

        if (batterStat.getAvg() > 1.0) {
            int totalHits = batterStat.getHit() + batterStat.getH_double() + batterStat.getTriple() + batterStat.getHr();
            double avg = totalHits / (batterStat.getAb() * 1.0);
            BigDecimal roundedAvg = new BigDecimal(avg).setScale(3, RoundingMode.HALF_UP);
            batterStat.setAvg(roundedAvg.doubleValue());

            if (roundedAvg.doubleValue() > 1.0) {
                throw new IllegalArgumentException("AVG cannot exceed 1.0");
            }
        }

        if (batterStat.getOps() > 5.0) {
            double ops = batterStat.getObp() + batterStat.getSlg();
            BigDecimal roundedOps = new BigDecimal(ops).setScale(3, RoundingMode.HALF_UP);
            batterStat.setOps(roundedOps.doubleValue());

            if (roundedOps.doubleValue() > 5.0) {
                throw new IllegalArgumentException("OPS cannot exceed 5.0");
            }
        }
    }
}
