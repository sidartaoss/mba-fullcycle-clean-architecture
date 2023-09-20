package br.com.fullcycle.application.event;

import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.PartnerRepository;

import java.util.Objects;

public class CreateEventUseCase
        extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

    private final EventRepository eventRepository;
    private final PartnerRepository partnerRepository;

    public CreateEventUseCase(final EventRepository eventRepository, final PartnerRepository partnerRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    public record Input(
            String date,
            String name,
            Integer totalSpots,
            String partnerId
    ) {

        public static Input with(
                final String aDate, final String aName, final Integer aTotalSpots, final String aPartnerId
        ) {
            return new Input(aDate, aName, aTotalSpots, aPartnerId);
        }
    }

    public record Output(
            String id,
            String date,
            String name,
            int totalSpots,
            String partnerId
    ) {

        public static Output with(
                final String anId,
                final String aDate,
                final String aName,
                final int aTotalSpots,
                final String aPartnerId) {
            return new Output(anId, aDate, aName, aTotalSpots, aPartnerId);
        }
    }

    @Override
    public Output execute(final Input input) {
        final var aPartner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId()))
                        .orElseThrow(() -> new ValidationException("Partner not found"));

        final var event = this.eventRepository.create(
                Event.newEvent(input.name(), input.date(), input.totalSpots, aPartner));

        return Output
                .with(event.eventId().value(),
                        input.date(),
                        event.name().value(),
                        event.totalSpots(),
                        event.partnerId().value());
    }
}
