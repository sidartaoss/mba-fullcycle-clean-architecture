package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicket;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

    @Id
    private UUID eventTickedId;

    private UUID ticketId;

    private UUID customerId;

    private int ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventEntity event;

    public EventTicketEntity() {
    }

    private EventTicketEntity(
            final UUID anEventTicketId,
            final UUID aCustomerId,
            final int anOrdering,
            final UUID aTicketId,
            final EventEntity anEvent
            ) {
        this.eventTickedId = anEventTicketId;
        this.customerId = aCustomerId;
        this.ordering = anOrdering;
        this.ticketId = aTicketId;
        this.event = anEvent;
    }

    public static EventTicketEntity of(
            final EventEntity anEvent, final EventTicket anEventTicket) {
        return new EventTicketEntity(
                UUID.fromString(anEventTicket.eventTicketId().value()),
                UUID.fromString(anEventTicket.customerId().value()),
                anEventTicket.ordering(),
                tickedIdIfNonNull(anEventTicket),
                anEvent
        );
    }

    public EventTicket toDomain() {
        return EventTicket.with(
                EventTicketId.with(getEventTickedId().toString()),
                EventId.with(getEvent().getId().toString()),
                CustomerId.with(getCustomerId().toString()),
                ticketIdIfNonNull(),
                getOrdering()
        );
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public UUID getEventTickedId() {
        return eventTickedId;
    }

    public void setEventTickedId(UUID eventTickedId) {
        this.eventTickedId = eventTickedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTicketEntity that = (EventTicketEntity) o;
        return Objects.equals(eventTickedId, that.eventTickedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTickedId);
    }

    private static UUID tickedIdIfNonNull(final EventTicket anEventTicket) {
        return anEventTicket.ticketId() != null ? UUID.fromString(anEventTicket.ticketId().value()) : null;
    }

    private TicketId ticketIdIfNonNull() {
        return getTicketId() != null ? TicketId.with(getTicketId().toString()) : null;
    }
}
