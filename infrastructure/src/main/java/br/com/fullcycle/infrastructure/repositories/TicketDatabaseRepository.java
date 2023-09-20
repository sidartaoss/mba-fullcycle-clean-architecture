package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketRepository;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.TicketJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class TicketDatabaseRepository implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper mapper;

    public TicketDatabaseRepository(final TicketJpaRepository ticketJpaRepository, OutboxJpaRepository outboxJpaRepository, ObjectMapper mapper) {
        this.ticketJpaRepository = Objects.requireNonNull(ticketJpaRepository);
        this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public Optional<Ticket> ticketOfId(final TicketId anId) {
        Objects.requireNonNull(anId, "Id cannot be null");
        return this.ticketJpaRepository.findById(UUID.fromString(anId.value()))
                .map(TicketEntity::toDomain);
    }

    @Transactional
    @Override
    public Ticket create(final Ticket aTicket) {
        return save(aTicket);
    }

    @Transactional
    @Override
    public Ticket update(final Ticket aTicket) {
        return save(aTicket);
    }

    @Override
    public void deleteAll() {
        this.ticketJpaRepository.deleteAll();
    }

    private Ticket save(Ticket aTicket) {
        this.outboxJpaRepository.saveAll(
                aTicket.allDomainEvents()
                        .stream().map(it -> OutboxEntity.of(it, this::toJson))
                        .toList()
        );
        return this.ticketJpaRepository.save(TicketEntity.of(aTicket))
                .toDomain();
    }

    private String toJson(final DomainEvent aDomainEvent) {
        try {
            return this.mapper.writeValueAsString(aDomainEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
