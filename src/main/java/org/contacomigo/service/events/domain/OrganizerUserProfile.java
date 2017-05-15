package org.contacomigo.service.events.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrganizerUserProfile.
 */
@Entity
@Table(name = "organizer_user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizerUserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrganizerUserProfile organizerUserProfile = (OrganizerUserProfile) o;
        if (organizerUserProfile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, organizerUserProfile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrganizerUserProfile{" +
            "id=" + id +
            '}';
    }
}
