package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CreateCustomerUseCaseIT extends IntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() throws Exception {
        // Given
        final var expectedCpf = "264.385.720-80";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var useCase = new CreateCustomerUseCase(customerRepository);

        final var command = CreateCustomerUseCase.Input
                .with(expectedCpf, expectedEmail, expectedName);

        // When
        final var actualOutput = useCase.execute(command);

        // Then
        assertNotNull(actualOutput.id());
        Assertions.assertEquals(expectedCpf, actualOutput.cpf());
        Assertions.assertEquals(expectedEmail, actualOutput.email());
        Assertions.assertEquals(expectedName, actualOutput.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        // Given
        final var expectedCpf = "264.385.720-80";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        createCustomer(expectedName, expectedCpf, "sidarta@silva.com");

        final var useCase = new CreateCustomerUseCase(customerRepository);

        final var command = CreateCustomerUseCase.Input
                .with(expectedCpf, expectedEmail, expectedName);

        final var expectedErrorMessage = "Customer already exists";

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // Given
        final var expectedCpf = "264.385.720-80";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        createCustomer(
                expectedName, "715.683.950-00", expectedEmail);

        final var useCase = new CreateCustomerUseCase(customerRepository);

        final var command = CreateCustomerUseCase.Input
                .with(expectedCpf, expectedEmail, expectedName);

        final var expectedErrorMessage = "Customer already exists";

        // When
        Executable invalidMethodCall = () -> useCase.execute(command);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private Customer createCustomer(
            final String expectedName,
            final String expectedCpf,
            final String expectedEmail) {
        final var aCustomer = Customer.newCustomer(expectedName, expectedCpf, expectedEmail);
        return customerRepository.create(aCustomer);
    }
}
