package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.infrastructure.dtos.SubscribeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "events")
public class EventController {

    private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

    public EventController(
            final CreateEventUseCase createEventUseCase,
            final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase) {
        this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
        this.subscribeCustomerToEventUseCase = Objects.requireNonNull(subscribeCustomerToEventUseCase);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<?> create(@RequestBody NewEventDTO dto) {
        try {
            final var input = CreateEventUseCase.Input
                    .with(dto.date(), dto.name(), dto.totalSpots(), dto.partnerId().toString());
            final var output = createEventUseCase.execute(input);
            return ResponseEntity.created(URI.create("/events/" + output.id()))
                    .body(output);
        } catch (ValidationException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable String id, @RequestBody SubscribeDTO dto) {
        try {
            final var input = SubscribeCustomerToEventUseCase.Input.with(id, dto.customerId());
            final var output = subscribeCustomerToEventUseCase.execute(input);
            return ResponseEntity.ok(output);
        } catch (ValidationException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(e.getMessage());
        }
    }
}
