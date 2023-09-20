package br.com.fullcycle.application.partner;

import br.com.fullcycle.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // Given
        final var expectedCnpj = "90.113.692/0001-77";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var command = CreatePartnerUseCase.Input
                .with(expectedCnpj, expectedEmail, expectedName);

        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new CreatePartnerUseCase(partnerRepository);

        // When
        final var actualOutput = useCase.execute(command);

        // Then
        assertNotNull(actualOutput.id());
        assertEquals(expectedCnpj, actualOutput.cnpj());
        assertEquals(expectedEmail, actualOutput.email());
        assertEquals(expectedName, actualOutput.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() {
        // Given
        final var expectedCnpj =  "90.113.692/0001-77";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName =  "John Doe";

        final var expectedErrorMessage = "Partner already exists";

        final var aPartner = Partner.newPartner(
                expectedName, expectedCnpj, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var useCase = new CreatePartnerUseCase(partnerRepository);

        final var command = CreatePartnerUseCase.Input
                .with(expectedCnpj, "john2@gmail.com", expectedName);

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() {
        // Given
        final var expectedCnpj =  "90.113.692/0001-77";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var expectedErrorMessage = "Partner already exists";

        final var aPartner = Partner.newPartner(
                expectedName, expectedCnpj, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var useCase = new CreatePartnerUseCase(partnerRepository);

        final var command = CreatePartnerUseCase.Input
                .with("07.364.101/0001-90", expectedEmail, expectedName);

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
