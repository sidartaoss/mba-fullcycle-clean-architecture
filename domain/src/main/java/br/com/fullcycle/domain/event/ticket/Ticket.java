package br.com.fullcycle.domain.event.ticket;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.exceptions.ValidationException;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Ticket {

    private final TicketId ticketId;
    private final Set<DomainEvent> domainEvents;

    private CustomerId customerId;
    private EventId eventId;
    private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;

    private Ticket(
            final TicketId aTicketId,
            final CustomerId aCustomerId,
            final EventId anEventId,
            final TicketStatus aStatus,
            final Instant aReservedAt) {
        this.ticketId = aTicketId;
        this.domainEvents = new HashSet<>();
        this.changeCustomerId(aCustomerId);
        this.changeEventId(anEventId);
        this.changeStatus(aStatus);
        this.changeReservedAt(aReservedAt);
    }

    private Ticket(
            final TicketId aTicketId,
            final CustomerId aCustomerId,
            final EventId anEventId,
            final TicketStatus aStatus,
            final Instant aPaidAt,
            final Instant aReservedAt) {
        this(aTicketId, aCustomerId, anEventId, aStatus, aReservedAt);
        this.changePaidAt(aPaidAt);
    }

    public static Ticket newTicket(final CustomerId aCustomerId, final EventId anEventId) {
        return new Ticket(
                TicketId.unique(),
                aCustomerId,
                anEventId,
                TicketStatus.PENDING,
                Instant.now());
    }

    public static Ticket newTicket(
            final EventTicketId anEventTicketId, final CustomerId aCustomerId, final EventId anEventId) {
        final var aTicket = newTicket(aCustomerId, anEventId);
        aTicket.domainEvents.add(new TicketCreated(aTicket.ticketId, anEventTicketId, anEventId, aCustomerId));
        return aTicket;
    }

    public static Ticket with(
            final TicketId anId,
            final CustomerId aCustomerId,
            final EventId anEventId,
            final TicketStatus aStatus,
            final Instant aPaidAt,
            final Instant aReservedAt
    ) {
        return new Ticket(
                anId,
                aCustomerId,
                anEventId,
                aStatus,
                aPaidAt,
                aReservedAt);
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public EventId eventId() {
        return eventId;
    }

    public TicketStatus status() {
        return status;
    }

    public Instant paidAt() {
        return paidAt;
    }

    public Instant reservedAt() {
        return reservedAt;
    }

    public Set<DomainEvent> allDomainEvents() {
        return Collections.unmodifiableSet(domainEvents);
    }

    private void changeCustomerId(final CustomerId aCustomerId) {
        if (aCustomerId == null) {
            throw new ValidationException("Invalid customerId for Ticket");
        }
        this.customerId = aCustomerId;
    }

    private void changeEventId(final EventId anEventId) {
        if (anEventId == null) {
            throw new ValidationException("Invalid eventId for Ticket");
        }
        this.eventId = anEventId;
    }

    private void changeStatus(final TicketStatus aStatus) {
        if (aStatus == null) {
            throw new ValidationException("Invalid status for Ticket");
        }
        this.status = aStatus;
    }

    private void changePaidAt(final Instant aPaidAt) {
        this.paidAt = aPaidAt;
    }

    private void changeReservedAt(final Instant aReservedAt) {
        if (aReservedAt == null) {
            throw new ValidationException("Invalid reservedAt for Ticket");
        }
        this.reservedAt = aReservedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
}
