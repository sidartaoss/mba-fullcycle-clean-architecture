package br.com.fullcycle.application.partner;

import br.com.fullcycle.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetPartnerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() throws Exception {
        // Given
        final var expectedCnpj = "02.493.730/0001-05";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = Partner.newPartner(
                expectedName, expectedCnpj, expectedEmail);

        final var expectedId = aPartner.partnerId().value().toString();

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var useCase = new GetPartnerByIdUseCase(partnerRepository);

        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        // When & Then
        useCase.execute(input)
                .ifPresent(actualOutput -> {
                    assertEquals(expectedId, actualOutput.id());
                    assertEquals(expectedCnpj, actualOutput.cnpj());
                    assertEquals(expectedEmail, actualOutput.email());
                    assertEquals(expectedName, actualOutput.name());
                });
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
    public void testGetByIdWithInvalidId() throws Exception {
        // Given
        final var expectedId = PartnerId.unique().value();

        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);

        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        // When
        final var actualOutput = useCase.execute(input);

        // Then
        assertTrue(actualOutput.isEmpty());
    }
}