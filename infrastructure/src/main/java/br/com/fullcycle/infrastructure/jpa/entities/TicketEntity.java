package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Ticket")
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private UUID eventId;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private Instant paidAt;

    private Instant reservedAt;

    public TicketEntity() {
    }

    private TicketEntity(
            final UUID anId,
            final UUID aCustomerId,
            final UUID anEventId,
            final TicketStatus aStatus,
            final Instant aPaidAt,
            final Instant aReservedAt) {
        this.id = anId;
        this.customerId = aCustomerId;
        this.eventId = anEventId;
        this.status = aStatus;
        this.paidAt = aPaidAt;
        this.reservedAt = aReservedAt;
    }

    public static TicketEntity of(Ticket aTicket) {
        return new TicketEntity(
                UUID.fromString(aTicket.ticketId().value()),
                UUID.fromString(aTicket.customerId().value()),
                UUID.fromString(aTicket.eventId().value()),
                aTicket.status(),
                aTicket.paidAt(),
                aTicket.reservedAt()
        );
    }

    public Ticket toDomain() {
        return Ticket.with(
                TicketId.with(getId().toString()),
                CustomerId.with(getCustomerId().toString()),
                EventId.with(getEventId().toString()),
                getStatus(),
                getPaidAt(),
                getReservedAt());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
