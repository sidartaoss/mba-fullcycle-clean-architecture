package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    @DisplayName("Deve instanciar um CPF válido")
    public void testCreateCpf() throws Exception {
        // Given
        final var expectedCpf = "774.839.010-04";

        // When
        final var actualCnpj = new Cpf(expectedCpf);

        // Then
        assertNotNull(actualCnpj);
        assertEquals(expectedCpf, actualCnpj.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF inválido")
    public void testCreateCpfWithInvalidValue() throws Exception {
        // Given
        final var anInvalidCpf = "774.83901004";

        final var expectedErrorMessage = "Invalid value for Cpf";

        // When
        Executable invalidMethodCall = () -> new Cpf(anInvalidCpf);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF nulo")
    public void testCreateCpfWithInvalidNullValue() throws Exception {
        // Given
        final String anInvalidNullCpf = null;

        final var expectedErrorMessage = "Invalid value for Cpf";

        // When
        Executable invalidMethodCall = () -> new Cpf(anInvalidNullCpf);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}