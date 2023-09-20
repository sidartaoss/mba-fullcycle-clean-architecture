package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("Deve instanciar um Email válido")
    public void testCreateEmail() throws Exception {
        // Given
        final var expectedEmail = "john.doe@gmail.com";

        // When
        final var actualEmail = new Email(expectedEmail);

        // Then
        assertNotNull(actualEmail);
        assertEquals(expectedEmail, actualEmail.value());
    }

    @Test
    @DisplayName("Não deve instanciar um Email inválido")
    public void testCreateEmailWithInvalidValue() throws Exception {
        // Given
        final var anInvalidEmail = "john.doe.gmail.com";

        final var expectedErrorMessage = "Invalid value for Email";

        // When
        Executable invalidMethodCall = () -> new Email(anInvalidEmail);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um Email nulo")
    public void testCreateEmailWithInvalidNullValue() throws Exception {
        // Given
        final String anInvalidNullEmail = null;

        final var expectedErrorMessage = "Invalid value for Email";

        // When
        Executable invalidMethodCall = () -> new Email(anInvalidNullEmail);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}