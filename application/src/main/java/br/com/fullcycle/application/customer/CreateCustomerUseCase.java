package br.com.fullcycle.application.customer;

import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.customer.CustomerRepository;

import java.util.Objects;

public class CreateCustomerUseCase
        extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(final CustomerRepository customerRepository) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
    }

    public record Input(
            String cpf,
            String email,
            String name
    ) {
        public static Input with(final String cpf, final String email, final String name) {
            return new Input(cpf, email, name);
        }
    }

    public record Output(
            String id,
            String cpf,
            String email,
            String name
    ) {
        public static Output from(final Customer customer) {
            return new Output(
                    customer.customerId().value(),
                    customer.cpf().value(),
                    customer.email().value(),
                    customer.name().value()
            );
        }
    }

    @Override
    public Output execute(final Input input) {
        if (customerRepository.customerOfCpf(new Cpf(input.cpf())).isPresent()) {
            throw new ValidationException("Customer already exists");
        }
            if (customerRepository.customerOfEmail(new Email(input.email())).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        final var customer = Customer
                .newCustomer(
                        input.name(),
                        input.cpf(),
                        input.email());

        return Output.from(
                customerRepository.create(customer));
    }
}
