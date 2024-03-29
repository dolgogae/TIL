package hellojpa;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;

@Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
