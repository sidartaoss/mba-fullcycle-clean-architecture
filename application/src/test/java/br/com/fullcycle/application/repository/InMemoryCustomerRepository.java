package br.com.fullcycle.application.repository;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.customer.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<String, Customer> customersByCpf;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCpf = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    private final Map<String, Customer> customersByEmail;

    @Override
    public Optional<Customer> customerOfId(CustomerId anId) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(anId).value()));
    }

    @Override
    public Optional<Customer> customerOfCpf(Cpf aCpf) {
        return Optional.ofNullable(this.customersByCpf.get(aCpf.value()));
    }

    @Override
    public Optional<Customer> customerOfEmail(Email anEmail) {
        return Optional.ofNullable(this.customersByEmail.get(anEmail.value()));
    }

    @Override
    public Customer create(final Customer customer) {
        this.customers.put(customer.customerId().value(), customer);
        this.customersByCpf.put(customer.cpf().value(), customer);
        this.customersByEmail.put(customer.email().value(), customer);
        return customer;
    }

    @Override
    public Customer update(final Customer customer) {
        this.customers.put(customer.customerId().value(), customer);
        this.customersByCpf.put(customer.cpf().value(), customer);
        this.customersByEmail.put(customer.email().value(), customer);
        return customer;
    }

    @Override
    public void deleteAll() {
        this.customers.clear();
        this.customersByCpf.clear();
        this.customersByEmail.clear();
    }
}
