package br.com.fullcycle.application.partner;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;

import java.util.Objects;
import java.util.Optional;

public class GetPartnerByIdUseCase
        extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

    private final PartnerRepository partnerRepository;

    public GetPartnerByIdUseCase(final PartnerRepository partnerRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    public record Input(
            String id
    ) {

        public static Input with(final String anId) {
            return new Input(anId);
        }
    }

    public record Output(
            String id,
            String cnpj,
            String email,
            String name
    ) {

        public static Output from(final Partner partner) {
            return new Output(
                    partner.partnerId().value(),
                    partner.cnpj().value(),
                    partner.email().value(),
                    partner.name().value()
            );
        }
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return partnerRepository.partnerOfId(PartnerId.with(input.id()))
                .map(Output::from);
    }
}
