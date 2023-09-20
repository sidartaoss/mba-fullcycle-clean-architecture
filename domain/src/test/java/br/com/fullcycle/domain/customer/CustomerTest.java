package br.com.fullcycle.domain.customer;

import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    @DisplayName("Deve instanciar um cliente")
    public void testCreateCustomer() throws Exception {
        // Given
        final var expectedCpf = "926.400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        // When
        final var actualCustomer = Customer.newCustomer(
                expectedName, expectedCpf, expectedEmail);

        // Then
        assertNotNull(actualCustomer.customerId());
        assertEquals(expectedCpf, actualCustomer.cpf().value());
        assertEquals(expectedEmail, actualCustomer.email().value());
        assertEquals(expectedName, actualCustomer.name().value());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com CPF inválido")
    public void testCreateCustomerWithInvalidCpf() throws Exception {
        // Given
        final var anInvalidCpf = "926400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var expectedErrorMessage = "Invalid value for Cpf";

        // When
        Executable invalidMethodCall = () -> Customer.newCustomer(
                expectedName, anInvalidCpf, expectedEmail);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com nome inválido")
    public void testCreateCustomerWithInvalidName() throws Exception {
        // Given
        final var expectedCpf = "926400.290-10";
        final var expectedEmail = "john.doe@gmail.com";
        final String anInvalidNullName = null;

        final var expectedErrorMessage = "Invalid value for Name";

        // When
        Executable invalidMethodCall = () -> Customer.newCustomer(
                anInvalidNullName, expectedCpf, expectedEmail);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @DisplayName("Não deve instanciar um cliente com email inválido")
    public void testCreateCustomerWithInvalidEmail() throws Exception {
        // Given
        final var expectedCpf = "926400.290-10";
        final var anInvalidEmail = "john.doe_gmail.com";
        final String expectedName = "John Doe";

        final var expectedErrorMessage = "Invalid value for Email";

        // When
        Executable invalidMethodCall = () -> Customer.newCustomer(
                expectedName, expectedCpf, anInvalidEmail);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
