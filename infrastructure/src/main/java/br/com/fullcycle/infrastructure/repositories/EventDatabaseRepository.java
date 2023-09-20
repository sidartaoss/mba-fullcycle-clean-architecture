package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class EventDatabaseRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper mapper;

    public EventDatabaseRepository(final EventJpaRepository eventJpaRepository,
                                   final OutboxJpaRepository outboxJpaRepository,
                                   final ObjectMapper aMapper) {
        this.eventJpaRepository = Objects.requireNonNull(eventJpaRepository);
        this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
        this.mapper = aMapper;
    }

    @Override
    public Optional<Event> eventOfId(final EventId anId) {
        Objects.requireNonNull(anId, "Id cannot be null");
        return this.eventJpaRepository.findById(UUID.fromString(anId.value()))
                .map(EventEntity::toDomain);
    }

    @Transactional
    @Override
    public Event create(final Event anEvent) {
        return save(anEvent);
    }

    @Transactional
    @Override
    public Event update(final Event anEvent) {
        return save(anEvent);
    }

    @Override
    public void deleteAll() {
        this.eventJpaRepository.deleteAll();
    }

    private String toJson(final DomainEvent aDomainEvent) {
        try {
            return this.mapper.writeValueAsString(aDomainEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Event save(Event anEvent) {
        this.outboxJpaRepository.saveAll(
                anEvent.allDomainEvents()
                        .stream().map(it -> OutboxEntity.of(it, this::toJson))
                        .toList()
        );
        return this.eventJpaRepository.save(EventEntity.of(anEvent))
                .toDomain();
    }
}
