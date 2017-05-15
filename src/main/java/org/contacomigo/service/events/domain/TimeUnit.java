package org.contacomigo.service.events.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.contacomigo.service.events.domain.enumeration.TimeUnits;
import org.contacomigo.service.events.service.util.RandomUtil;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unitTime == null) ? 0 : unitTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeUnit other = (TimeUnit) obj;
		if (unitTime != other.unitTime)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeUnit [unitTime=" + unitTime + "]";
	}   
    
}