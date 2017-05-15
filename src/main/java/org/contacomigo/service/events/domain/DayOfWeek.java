package org.contacomigo.service.events.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.contacomigo.service.events.domain.enumeration.DaysOfWeek;
import org.contacomigo.service.events.service.util.RandomUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DayOfWeek.
 */
@Entity
@Table(name = "day_of_week")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DayOfWeek implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "day", nullable = false)
	private DaysOfWeek day;

	@ManyToOne
	private Period period;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
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
		DayOfWeek other = (DayOfWeek) obj;
		if (day != other.day)
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DayOfWeek [day=" + day + ", period=" + period + "]";
	}

}
