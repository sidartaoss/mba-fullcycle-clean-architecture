package br.com.fullcycle.infrastructure.graphql;

import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewPartnerDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class PartnerResolver {

    private final CreatePartnerUseCase createPartnerUseCase;

    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerResolver(
            final CreatePartnerUseCase createPartnerUseCase,
            final GetPartnerByIdUseCase getPartnerByIdUseCase) {
        this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
        this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
    }

    @MutationMapping
    public NewPartnerDTO createPartner(@Argument NewPartnerDTO input) {
        final var command = CreatePartnerUseCase.Input
                .with(input.cnpj(),
                        input.email(),
                        input.name());
        final var output = createPartnerUseCase.execute(command);
        return NewPartnerDTO
                .with(output.id(),
                        output.name(),
                        output.cnpj(),
                        output.email());
    }

    @QueryMapping
    public NewPartnerDTO partnerOfId(@Argument String id) {
        final var input = GetPartnerByIdUseCase.Input.with(id);
        return getPartnerByIdUseCase.execute(input)
                .map(NewPartnerDTO::from)
                .orElse(null);
    }
}
