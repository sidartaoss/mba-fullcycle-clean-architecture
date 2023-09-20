package br.com.fullcycle.domain.event.ticket;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;

import java.time.Instant;
import java.util.UUID;

public record TicketCreated(
        String domainEventId,
        String type,
        String ticketId,
        String eventTickedId,
        String eventId,
        String customerId,
        Instant occurredOn
) implements DomainEvent {

    public TicketCreated(
            TicketId aTicketId,
            EventTicketId anEventTicketId,
            EventId anEventId,
            CustomerId aCustomerId) {
        this(UUID.randomUUID().toString(), "ticket.created", aTicketId.value(), anEventTicketId.value(),
                anEventId.value(), aCustomerId.value(), Instant.now());
    }
}
