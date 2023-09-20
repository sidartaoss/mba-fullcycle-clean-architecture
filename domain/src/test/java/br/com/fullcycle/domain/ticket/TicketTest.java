package br.com.fullcycle.domain.ticket;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.event.ticket.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    @DisplayName("Deve criar um ticket")
    public void testCreateTicket() throws Exception {
        // Given
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

        final var expectedEventDate = "2021-01-01";
        final var expectedEventName = "Disney on Ice";
        final var expectedEventTotalSpots = 10;

        final var anEvent = Event.newEvent(
                expectedEventName, expectedEventDate, expectedEventTotalSpots, aPartner);
        final var expectedEventId = anEvent.eventId();

        final var expectedTicketStatus = TicketStatus.PENDING;

        // When
        final var actualTicket = Ticket.newTicket(expectedCustomerId, expectedEventId);

        // Then
        assertNotNull(actualTicket.ticketId());
        assertNotNull(actualTicket.reservedAt());
        assertNull(actualTicket.paidAt());
        assertEquals(expectedEventId, actualTicket.eventId());
        assertEquals(expectedCustomerId, actualTicket.customerId());
        assertEquals(expectedTicketStatus, actualTicket.status());
    }
}