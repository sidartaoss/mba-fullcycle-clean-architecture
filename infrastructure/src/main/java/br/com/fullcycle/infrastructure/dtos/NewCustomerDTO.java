package br.com.fullcycle.infrastructure.dtos;

import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.domain.customer.Customer;

public record NewCustomerDTO(
        String id,
        String name,
        String cpf,
        String email
) {

    public static NewCustomerDTO with(
            final String id,
            final String name,
            final String cpf,
            final String email
    ) {
        return new NewCustomerDTO(id, name, cpf, email);
    }

    public static NewCustomerDTO with(
            final String name,
            final String cpf,
            final String email
    ) {
        return new NewCustomerDTO(null, name, cpf, email);
    }

    public static NewCustomerDTO from(final Customer customer) {
        return new NewCustomerDTO(
                customer.customerId().value().toString(),
                customer.name().value(), customer.cpf().value(), customer.email().value());
    }

    public static NewCustomerDTO from(final GetCustomerByIdUseCase.Output dto) {
        return new NewCustomerDTO(dto.id(), dto.name(), dto.cpf(), dto.email());
    }
}
