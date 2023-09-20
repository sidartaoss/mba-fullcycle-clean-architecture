package br.com.fullcycle.application.event;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.application.repository.InMemoryEventRepository;
import br.com.fullcycle.application.repository.InMemoryPartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        // Given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;

        final var expectedPartnerCnpj =  "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);
        final var expectedPartnerId = aPartner.partnerId().value();

        final var eventRepository = new InMemoryEventRepository();
        final var partnerRepository = new InMemoryPartnerRepository();

        partnerRepository.create(aPartner);

        final var command = CreateEventUseCase.Input
                .with(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        final var useCase = new CreateEventUseCase(eventRepository, partnerRepository);

        // When
        final var output = useCase.execute(command);

        // Then
        Assertions.assertNotNull(output.id());
        assertEquals(expectedDate, output.date());
        assertEquals(expectedName, output.name());
        assertEquals(expectedTotalSpots, output.totalSpots());
        assertEquals(expectedPartnerId, output.partnerId());
    }

    @Test
    @DisplayName("Deve criar um evento quando o Partner não for encontrado")
    public void testCreateEvent_whenPartnerDoesNotExist_ThenShouldThrowError() throws Exception {
        // Given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = PartnerId.unique().value();

        final var command = CreateEventUseCase.Input
                .with(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        final var eventRepository = new InMemoryEventRepository();
        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new CreateEventUseCase(eventRepository, partnerRepository);

        final var expectedErrorMessage = "Partner not found";

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}