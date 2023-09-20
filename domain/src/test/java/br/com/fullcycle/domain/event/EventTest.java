package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        // Given
        final var expectedEventDate = "2021-01-01";
        final var expectedEventName = "Disney on Ice";
        final var expectedEventTotalSpots = 10;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);
        final var expectedPartnerId = aPartner.partnerId().value();
        final var expectedTickets = 0;

        // When
        final var actualEvent = Event.newEvent(
                expectedEventName, expectedEventDate, expectedEventTotalSpots, aPartner);

        // Then
        Assertions.assertNotNull(actualEvent.eventId());
        assertEquals(LocalDate.parse(expectedEventDate, ISO_LOCAL_DATE), actualEvent.date());
        assertEquals(expectedEventName, actualEvent.name().value());
        assertEquals(expectedEventTotalSpots, actualEvent.totalSpots());
        assertEquals(expectedPartnerId, actualEvent.partnerId().value());
        assertEquals(expectedTickets, actualEvent.allTickets().size());
    }

    @Test
    @DisplayName("Não deve criar um evento com nome inválido")
    public void testCreateEventWithInvalidName() throws Exception {
        // Given
        final var expectedEventDate = "2021-01-01";
        final String anInvalidNullName = null;
        final var expectedEventTotalSpots = 10;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);

        final var expectedErrorMessage = "Invalid value for Name";

        // When
        Executable invalidMethodCall = () -> Event.newEvent(anInvalidNullName,
                expectedEventDate, expectedEventTotalSpots, aPartner);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um evento com data inválida")
    public void testCreateEventWithInvalidDate() throws Exception {
        // Given
        final String anInvalidNullDate = null;
        final var expectedEvenName = "Disney on Ice";
        final var expectedEventTotalSpots = 10;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);

        final var expectedErrorMessage = "Invalid date for Event";

        // When
        Executable invalidMethodCall = () -> Event.newEvent(expectedEvenName,
                anInvalidNullDate, expectedEventTotalSpots, aPartner);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve reservar um ticket quando é possível")
    public void testReserveTicket() throws Exception {
        // Given
        final var expectedEventDate = "2021-01-01";
        final var expectedEventName = "Disney on Ice";
        final var expectedEventTotalSpots = 10;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);
        final var expectedPartnerId = aPartner.partnerId().value();
        final var expectedTickets = 1;
        final var expectedTicketOrder = 1;
        final var expectedDomainEvent = "event-ticket.reserved";

        final var expectedCustomerCpf = "926.400.290-10";
        final var expectedCustomerEmail = "john.doe@gmail.com";
        final var expectedCustomerName = "John Doe";

        final var aCustomer = Customer.newCustomer(
                expectedCustomerName, expectedCustomerCpf, expectedCustomerEmail);
        final var expectedCustomerId = aCustomer.customerId();

        final var anEvent = Event.newEvent(
                expectedEventName, expectedEventDate, expectedEventTotalSpots, aPartner);
        final var expectedEventId = anEvent.eventId();

        // When
        final var actualTicket = anEvent.reserveTicket(expectedCustomerId);

        // Then
        assertNotNull(actualTicket.eventTicketId());
        assertNull(actualTicket.ticketId());
        assertEquals(expectedEventId, actualTicket.eventId());
        assertEquals(expectedCustomerId, actualTicket.customerId());

        assertEquals(expectedEventDate, anEvent.date().format(ISO_LOCAL_DATE));
        assertEquals(expectedEventName, anEvent.name().value());
        assertEquals(expectedEventTotalSpots, anEvent.totalSpots());
        assertEquals(expectedPartnerId, anEvent.partnerId().value());
        assertEquals(expectedTickets, anEvent.allTickets().size());

        final var actualEventTicket = anEvent.allTickets().iterator().next();
        assertEquals(expectedTicketOrder, actualEventTicket.ordering());
        assertEquals(expectedEventId, actualEventTicket.eventId());
        assertEquals(expectedCustomerId, actualEventTicket.customerId());
        assertEquals(actualTicket.ticketId(), actualEventTicket.ticketId());

        final var actualDomainEvents = anEvent.allDomainEvents().iterator().next();
        assertEquals(expectedDomainEvent, actualDomainEvents.type());
    }

    @Test
    @DisplayName("Não deve reservar um ticket quando o evento está esgotado")
    public void testReserveTicketWhenEventIsSoldOut() throws Exception {
        // Given
        final var expectedEventDate = "2021-01-01";
        final var expectedEventName = "Disney on Ice";
        final var expectedEventTotalSpots = 1;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);

        final var expectedCustomerCpf = "926.400.290-10";
        final var expectedCustomerEmail = "john.doe@gmail.com";
        final var expectedCustomerName = "John Doe";

        final var aCustomer = Customer.newCustomer(
                expectedCustomerName, expectedCustomerCpf, expectedCustomerEmail);
        final var expectedCustomerId = aCustomer.customerId();

        final var aCustomer2 = Customer.newCustomer(
                "Sidarta Silva", "912.904.880-02", "sidarta@silva.com");

        final var anEvent = Event.newEvent(
                expectedEventName, expectedEventDate, expectedEventTotalSpots, aPartner);

        anEvent.reserveTicket(expectedCustomerId);

        final var expectedErrorMessage = "Event sold out";

        // When
        Executable invalidMethodCall = () -> anEvent.reserveTicket(aCustomer2.customerId());

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve reservar dois tickets para um mesmo cliente")
    public void testReserveTwoTicketsForTheSameCustomer() throws Exception {
        // Given
        final var expectedEventDate = "2021-01-01";
        final var expectedEventName = "Disney on Ice";
        final var expectedEventTotalSpots = 1;

        final var expectedPartnerCnpj = "90.113.692/0001-77";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedPartnerName = "John Doe";
        final var aPartner = Partner.newPartner(
                expectedPartnerName, expectedPartnerCnpj, expectedPartnerEmail);

        final var expectedCustomerCpf = "926.400.290-10";
        final var expectedCustomerEmail = "john.doe@gmail.com";
        final var expectedCustomerName = "John Doe";

        final var aCustomer = Customer.newCustomer(
                expectedCustomerName, expectedCustomerCpf, expectedCustomerEmail);
        final var expectedCustomerId = aCustomer.customerId();

        final var anEvent = Event.newEvent(
                expectedEventName, expectedEventDate, expectedEventTotalSpots, aPartner);

        anEvent.reserveTicket(expectedCustomerId);

        final var expectedErrorMessage = "Email already registered";

        // When
        Executable invalidMethodCall = () -> anEvent.reserveTicket(expectedCustomerId);

        // Then
        final var actualException = assertThrows(ValidationException.class, invalidMethodCall);
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
