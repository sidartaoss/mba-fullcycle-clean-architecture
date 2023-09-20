package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewCustomerDTO;
import br.com.fullcycle.infrastructure.rest.presenters.GetCustomerByIdResponseEntity;
import br.com.fullcycle.infrastructure.rest.presenters.PublicGetCustomerByIdString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
        try {
            final var input = CreateCustomerUseCase.Input
                    .with(dto.cpf(),
                            dto.email(),
                            dto.name());
            final var output = createCustomerUseCase.execute(input);
            return ResponseEntity
                    .created(URI.create("/customers/" + output.id()))
                    .body(output);
        } catch (ValidationException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable String id, @RequestHeader(name = "X-PUBLIC", required = false) String xPublic) {
        Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> presenter = new GetCustomerByIdResponseEntity();
        if (xPublic != null && !xPublic.isBlank()) {
            presenter = new PublicGetCustomerByIdString();
        }
        final var input = GetCustomerByIdUseCase.Input.with(id);
        return getCustomerByIdUseCase.execute(input, presenter);
    }
}