package br.com.fullcycle.infrastructure.dtos;

import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;

public record NewPartnerDTO(
        String id,
        String name,
        String cnpj,
        String email
) {

    public static NewPartnerDTO from(final PartnerEntity partnerEntity) {
        return new NewPartnerDTO(
                partnerEntity.getId().toString(),
                partnerEntity.getName(),
                partnerEntity.getCnpj(),
                partnerEntity.getEmail());
    }

    public static NewPartnerDTO from(
            final GetPartnerByIdUseCase.Output output) {
        return new NewPartnerDTO(
                output.id(),
                output.name(),
                output.cnpj(),
                output.email()
        );
    }

    public static NewPartnerDTO with(
            final String anId,
            final String aName,
            final String aCnpj,
            final String anEmail
    ) {
        return new NewPartnerDTO(anId.toString(), aName, aCnpj, anEmail);
    }

    public static NewPartnerDTO with(
            final String aName,
            final String aCnpj,
            final String anEmail
    ) {
        return new NewPartnerDTO(null, aName, aCnpj, anEmail);
    }

    public static NewPartnerDTO with(final String anId) {
        return new NewPartnerDTO(anId, null, null, null);
    }
}
