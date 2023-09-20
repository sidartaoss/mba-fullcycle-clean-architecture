package br.com.fullcycle.application.customer;

import br.com.fullcycle.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CreateCustomerUseCaseTest {

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() throws Exception {
        // Given
        final var expectedCpf = "926.400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var customerRepository = new InMemoryCustomerRepository();

        final var useCase = new CreateCustomerUseCase(customerRepository);

        final var command = CreateCustomerUseCase.Input
                .with(expectedCpf, expectedEmail, expectedName);

        // When
        final var actualOutput = useCase.execute(command);

        // Then
        assertNotNull(actualOutput.id());
        assertEquals(expectedCpf, actualOutput.cpf());
        assertEquals(expectedEmail, actualOutput.email());
        assertEquals(expectedName, actualOutput.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        // Given
        final var expectedCpf = "926.400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var aCustomer = Customer
                .newCustomer(expectedName, expectedCpf, expectedEmail);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

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
        final var expectedCpf = "926.400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var aCustomer = Customer
                .newCustomer(expectedName, expectedCpf, expectedEmail);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

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
}
