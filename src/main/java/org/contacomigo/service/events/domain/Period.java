package org.contacomigo.service.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Period.
 */
@Entity
@Table(name = "period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Period implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "jhi_interval", nullable = false)
    private Integer interval;

    @OneToOne
    @JoinColumn(unique = true)
    private TimeUnit timeUnit;

    @OneToMany(mappedBy = "period")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DayOfWeek> days = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getInterval() {
        return interval;
    }

    public Period interval(Integer interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public Period timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Set<DayOfWeek> getDays() {
        return days;
    }

    public Period days(Set<DayOfWeek> dayOfWeeks) {
        this.days = dayOfWeeks;
        return this;
    }

    public Period addDays(DayOfWeek dayOfWeek) {
        this.days.add(dayOfWeek);
        dayOfWeek.setPeriod(this);
        return this;
    }

    public Period removeDays(DayOfWeek dayOfWeek) {
        this.days.remove(dayOfWeek);
        dayOfWeek.setPeriod(null);
        return this;
    }

    public void setDays(Set<DayOfWeek> dayOfWeeks) {
        this.days = dayOfWeeks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Period period = (Period) o;
        if (period.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, period.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Period{" +
            "id=" + id +
            ", interval='" + interval + "'" +
            '}';
    }
}
