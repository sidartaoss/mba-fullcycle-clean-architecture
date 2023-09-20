package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventTicket;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Entity(name = "Event")
@Table(name = "events")
public class EventEntity {

    @Id
    private UUID id;

    private String name;

    private LocalDate date;

    private int totalSpots;

    private UUID partnerId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "event")
    private Set<EventTicketEntity> tickets;

    public EventEntity() {
        this.tickets = new HashSet<>();
    }

    private EventEntity(
            final UUID anId,
            final String aName,
            final LocalDate aDate,
            final int aTotalSpots,
            final UUID aPartnerId) {
        this();
        this.id = anId;
        this.name = aName;
        this.date = aDate;
        this.totalSpots = aTotalSpots;
        this.partnerId = aPartnerId;
    }

    public static EventEntity with(
            final UUID anId,
            final String aName,
            final LocalDate aDate,
            final int aTotalSpots,
            final UUID aPartnerId
            ) {
        return new EventEntity(anId, aName, aDate, aTotalSpots, aPartnerId);
    }

    public static EventEntity of(final Event anEvent) {
        final var anEntity = new EventEntity(
            UUID.fromString(anEvent.eventId().value()),
            anEvent.name().value(),
            anEvent.date(),
            anEvent.totalSpots(),
            UUID.fromString(anEvent.partnerId().value())
        );
        anEvent.allTickets().forEach(anEntity::addTicket);
        return anEntity;
    }

    public Event toDomain() {
        return Event.with(
                getId().toString(),
                getName(),
                getDate().format(ISO_LOCAL_DATE),
                getTotalSpots(),
                getPartnerId().toString(),
                getTickets().stream()
                        .map(EventTicketEntity::toDomain)
                        .collect(Collectors.toSet())
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public UUID getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(UUID partnerId) {
        this.partnerId = partnerId;
    }

    public Set<EventTicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(Set<EventTicketEntity> tickets) {
        this.tickets = tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity eventEntity = (EventEntity) o;
        return Objects.equals(id, eventEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void addTicket(final EventTicket aTicket) {
        getTickets().add(EventTicketEntity.of(this, aTicket));
    }

}
