package br.com.fullcycle.application.partner;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.application.UseCase;

import java.util.Objects;

public class CreatePartnerUseCase
        extends UseCase<CreatePartnerUseCase.Input, CreatePartnerUseCase.Output> {

    private final PartnerRepository partnerRepository;

    public CreatePartnerUseCase(final PartnerRepository partnerRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    public record Input(
            String cnpj,
            String email,
            String name
    ) {

        public static Input with(
                final String cnpj,
                final String email,
                final String name
        ) {
            return new Input(cnpj, email, name);
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
    public Output execute(final Input input) {
        if (partnerRepository.partnerOfCnpj(new Cnpj(input.cnpj())).isPresent()) {
            throw new ValidationException("Partner already exists");
        }
        if (partnerRepository.partnerOfEmail(new Email(input.email())).isPresent()) {
            throw new ValidationException("Partner already exists");
        }

        var partner = Partner.newPartner(
                input.name(),
                input.cnpj(),
                input.email());

        return Output.from(partnerRepository.create(partner));
    }
}
