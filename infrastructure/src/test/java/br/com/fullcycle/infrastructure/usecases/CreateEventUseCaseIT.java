package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CreateEventUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateEventUseCase useCase;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setUp() {
        this.eventRepository.deleteAll();
        this.partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        // Given
        final var expectedPartnerCnpj = "89.929.094/0001-91";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var partner = createPartner(expectedPartnerCnpj, expectedPartnerEmail, expectedPartnerName);

        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = partner.partnerId();

        final var command = CreateEventUseCase.Input
                .with(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId.value());

        // When
        final var output = useCase.execute(command);

        // Then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedPartnerId.value(), output.partnerId());
    }

    @Test
    @DisplayName("Deve criar um evento quando o Partner não for encontrado")
    public void testCreateEvent_whenPartnerDoesNotExist_ThenShouldThrowError() throws Exception {
        // Given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = PartnerId.unique().value();

        final var command = CreateEventUseCase.Input
                .with(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        final var useCase = new CreateEventUseCase(eventRepository, partnerRepository);

        final var expectedErrorMessage = "Partner not found";

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private Partner createPartner(
            final String cnpj,
            final String email,
            final String name
    ) {
        return partnerRepository.create(
                Partner.newPartner(name, cnpj, email));
    }

}