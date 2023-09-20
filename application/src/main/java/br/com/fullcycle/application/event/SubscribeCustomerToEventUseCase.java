package br.com.fullcycle.application.event;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.event.EventTicket;
import br.com.fullcycle.domain.exceptions.ValidationException;

import java.time.Instant;
import java.util.Objects;

public class SubscribeCustomerToEventUseCase
        extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

    private final CustomerRepository customerRepository;
    private final EventRepository eventRepository;

    public SubscribeCustomerToEventUseCase(
            final CustomerRepository customerRepository,
            final EventRepository eventRepository
    ) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    public record Input(
            String eventId,
            String customerId
    ) {

        public static Input with(final String anEventId, final String aCustomerId) {
            return new Input(anEventId, aCustomerId);
        }
    }

    public record Output(
            String eventId,

            String eventTicketId,
            Instant reservationDate
    ) {
        public static Output with(
                final String anEventId,
                final String anEventTicketId,
                final Instant aReservationDate) {
            return new Output(anEventId, anEventTicketId, aReservationDate);
        }
    }

    @Override
    public Output execute(final Input input) {
        final var aCustomer = customerRepository.customerOfId(CustomerId.with(input.customerId()))
                .orElseThrow(() -> new ValidationException("Customer not found"));

        final var anEvent = eventRepository.eventOfId(EventId.with(input.eventId()))
                .orElseThrow(() -> new ValidationException("Event not found"));

        final var aCustomerId = aCustomer.customerId();
        final EventTicket aTicket = anEvent.reserveTicket(aCustomerId);

        eventRepository.update(anEvent);

        return Output.with(anEvent.eventId().value(),
                        aTicket.eventTicketId().value(),
                        Instant.now());
    }
}
