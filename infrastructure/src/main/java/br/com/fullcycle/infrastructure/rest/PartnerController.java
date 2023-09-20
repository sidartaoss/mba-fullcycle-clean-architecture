package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewPartnerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

//@RestController
//@RequestMapping(value = "partners")
public class PartnerController {

    private final CreatePartnerUseCase createPartnerUseCase;

    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerController(
            final CreatePartnerUseCase createPartnerUseCase,
            final GetPartnerByIdUseCase getPartnerByIdUseCase) {
        this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
        this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewPartnerDTO dto) {
        try {
            final var input = CreatePartnerUseCase.Input.with(dto.cnpj(), dto.email(), dto.name());
            final var output = createPartnerUseCase.execute(input);
            return ResponseEntity
                    .created(URI.create("/partners/" + output.id()))
                    .body(output);
        } catch (ValidationException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        final var input = GetPartnerByIdUseCase.Input.with(id);
        return getPartnerByIdUseCase.execute(input)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
