package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CnpjTest {

    @Test
    @DisplayName("Deve instanciar um CNPJ válido")
    public void testCreateCnpj() throws Exception {
        // Given
        final var expectedCnpj = "06.518.614/0001-46";

        // When
        final var actualCnpj = new Cnpj(expectedCnpj);

        // Then
        assertNotNull(actualCnpj);
        assertEquals(expectedCnpj, actualCnpj.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ inválido")
    public void testCreateCnpjWithInvalidValue() throws Exception {
        // Given
        final var anInvalidCnpj = "25.770.529000115";

        final var expectedErrorMessage = "Invalid value for Cnpj";

        // When
        Executable invalidMethodCall = () -> new Cnpj(anInvalidCnpj);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ nulo")
    public void testCreateCnpjWithInvalidNullValue() throws Exception {
        // Given
        final String anInvalidNullCnpj = null;

        final var expectedErrorMessage = "Invalid value for Cnpj";

        // When
        Executable invalidMethodCall = () -> new Cnpj(anInvalidNullCnpj);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}