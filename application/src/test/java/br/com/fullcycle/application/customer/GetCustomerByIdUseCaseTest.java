package br.com.fullcycle.application.customer;

import br.com.fullcycle.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.domain.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() throws Exception {
        // Given
        final var expectedCpf = "926.400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = Customer.newCustomer(
                expectedName, expectedCpf, expectedEmail);

        final var expectedId = aCustomer.customerId().value().toString();

        final var customerRepository = new InMemoryCustomerRepository();

        final var useCase = new GetCustomerByIdUseCase(customerRepository);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // When & Then
        useCase.execute(input)
                .ifPresent(actualOutput -> {
                    assertEquals(expectedId, actualOutput.id());
                    assertEquals(expectedCpf, actualOutput.cpf());
                    assertEquals(expectedEmail, actualOutput.email());
                    assertEquals(expectedName, actualOutput.name());
                });
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalidId() throws Exception {
        // Given
        final var expectedId = UUID.randomUUID().toString();

        final var customerRepository = new InMemoryCustomerRepository();

        final var useCase = new GetCustomerByIdUseCase(customerRepository);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // When
        final var actualOutput = useCase.execute(input);

        // Then
        assertTrue(actualOutput.isEmpty());
    }
}