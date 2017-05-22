package org.contacomigo.service.events.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Lob
    @Column(name = "description")
    private byte[] description;

    @Column(name = "description_content_type")
    private String descriptionContentType;

    @Column(name = "attachment")
    private String attachment;

    @OneToOne
    @JoinColumn(unique = true)
    private OrganizerUserProfile organizer;

    @OneToOne
    @JoinColumn(unique = true)
    private Schedule schedule;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_participant",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="participants_id", referencedColumnName="id"))
    private Set<ParticipantUserProfile> participants = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getDescription() {
        return description;
    }

    public Event description(byte[] description) {
        this.description = description;
        return this;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    public String getDescriptionContentType() {
        return descriptionContentType;
    }

    public Event descriptionContentType(String descriptionContentType) {
        this.descriptionContentType = descriptionContentType;
        return this;
    }

    public void setDescriptionContentType(String descriptionContentType) {
        this.descriptionContentType = descriptionContentType;
    }

    public String getAttachment() {
        return attachment;
    }

    public Event attachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public OrganizerUserProfile getOrganizer() {
        return organizer;
    }

    public Event organizer(OrganizerUserProfile organizerUserProfile) {
        this.organizer = organizerUserProfile;
        return this;
    }

    public void setOrganizer(OrganizerUserProfile organizerUserProfile) {
        this.organizer = organizerUserProfile;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Event schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Location getLocation() {
        return location;
    }

    public Event location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<ParticipantUserProfile> getParticipants() {
        return participants;
    }

    public Event participants(Set<ParticipantUserProfile> participantUserProfiles) {
        this.participants = participantUserProfiles;
        return this;
    }

    public Event addParticipant(ParticipantUserProfile participantUserProfile) {
        this.participants.add(participantUserProfile);
        return this;
    }

    public Event removeParticipant(ParticipantUserProfile participantUserProfile) {
        this.participants.remove(participantUserProfile);
        return this;
    }

    public void setParticipants(Set<ParticipantUserProfile> participantUserProfiles) {
        this.participants = participantUserProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", descriptionContentType='" + descriptionContentType + "'" +
            ", attachment='" + attachment + "'" +
            '}';
    }
}
