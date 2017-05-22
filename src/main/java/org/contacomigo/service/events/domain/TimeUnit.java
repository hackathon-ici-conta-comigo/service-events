package org.contacomigo.service.events.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.contacomigo.service.events.domain.enumeration.TimeUnits;

/**
 * A TimeUnit.
 */
@Entity
@Table(name = "time_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TimeUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_time", nullable = false)
    private TimeUnits unitTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TimeUnits getUnitTime() {
        return unitTime;
    }

    public TimeUnit unitTime(TimeUnits unitTime) {
        this.unitTime = unitTime;
        return this;
    }

    public void setUnitTime(TimeUnits unitTime) {
        this.unitTime = unitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeUnit timeUnit = (TimeUnit) o;
        if (timeUnit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timeUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TimeUnit{" +
            "id=" + id +
            ", unitTime='" + unitTime + "'" +
            '}';
    }
}
