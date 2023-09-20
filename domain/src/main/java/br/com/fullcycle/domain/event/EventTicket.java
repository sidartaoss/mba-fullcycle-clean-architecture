package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.exceptions.ValidationException;

public class EventTicket {

    private final EventTicketId eventTicketId;
    private final EventId eventId;
    private final CustomerId customerId;
    private TicketId ticketId;
    private int ordering;

    private EventTicket(
            final EventTicketId anEventTicketId,
            final EventId anEventId,
            final CustomerId aCustomerId,
            final Integer anOrdering) {
        if (anEventTicketId == null) {
            throw new ValidationException("Invalid eventTicketId for EventTicket");
        }
        if (anEventId == null) {
            throw new ValidationException("Invalid eventId for EventTicket");
        }
        if (aCustomerId == null) {
            throw new ValidationException("Invalid customerId for EventTicket");
        }
        this.eventTicketId = anEventTicketId;
        this.eventId = anEventId;
        this.customerId = aCustomerId;
        this.changeOrdering(anOrdering);
    }

    private EventTicket(
            final EventTicketId anEventTicketId,
            final EventId anEventId,
            final CustomerId aCustomerId,
            final TicketId aTicketId,
            final Integer anOrdering) {
        this(anEventTicketId, anEventId, aCustomerId, anOrdering);
        this.ticketId = aTicketId;
    }

    public static EventTicket with(
            final EventTicketId anEventTicketId,
            final EventId anEventId,
            final CustomerId aCustomerId,
            final TicketId aTicketId,
            final int anOrdering) {
        return new EventTicket(
                anEventTicketId,
                anEventId,
                aCustomerId,
                aTicketId,
                anOrdering
        );
    }

    public EventTicket associateTicket(final TicketId aTicketId) {
        this.ticketId = aTicketId;
        return this;
    }

    public static EventTicket newTicket(
            final EventId anEventId,
            final CustomerId aCustomerId,
            final int ordering
    ) {
        return new EventTicket(EventTicketId.unique(), anEventId, aCustomerId, ordering);
    }

    public EventTicketId eventTicketId() {
        return eventTicketId;
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public EventId eventId() {
        return eventId;
    }

    public int ordering() {
        return ordering;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void changeOrdering(final Integer anOrdering) {
        if (anOrdering == null) {
            throw new ValidationException("Invalid ordering for EventTicket");
        }
        this.ordering = anOrdering;
    }
}
