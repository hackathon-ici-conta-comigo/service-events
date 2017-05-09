package org.contacomigo.service.events.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.contacomigo.service.events.domain.enumeration.DaysOfWeek;

/**
 * A DayOfWeek.
 */
@Entity
@Table(name = "day_of_week")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DayOfWeek implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private DaysOfWeek day;

    @ManyToOne
    private Period period;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DaysOfWeek getDay() {
        return day;
    }

    public DayOfWeek day(DaysOfWeek day) {
        this.day = day;
        return this;
    }

    public void setDay(DaysOfWeek day) {
        this.day = day;
    }

    public Period getPeriod() {
        return period;
    }

    public DayOfWeek period(Period period) {
        this.period = period;
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DayOfWeek dayOfWeek = (DayOfWeek) o;
        if (dayOfWeek.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dayOfWeek.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DayOfWeek{" +
            "id=" + id +
            ", day='" + day + "'" +
            '}';
    }
}
