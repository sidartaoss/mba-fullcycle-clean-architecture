package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;

import java.time.Instant;
import java.util.UUID;

public record EventTicketReserved(
        String domainEventId,
        String type,
        String eventTickedId,
        String eventId,
        String customerId,
        Instant occurredOn
) implements DomainEvent {

    public EventTicketReserved(
            EventTicketId anEventTicketId,
            EventId anEventId,
            CustomerId aCustomerId) {
        this(UUID.randomUUID().toString(), "event-ticket.reserved", anEventTicketId.value(),
                anEventId.value(), aCustomerId.value(), Instant.now());
    }
}
