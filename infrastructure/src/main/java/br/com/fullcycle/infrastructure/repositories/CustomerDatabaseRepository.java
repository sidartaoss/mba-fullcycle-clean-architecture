package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerDatabaseRepository implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerDatabaseRepository(final CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = Objects.requireNonNull(customerJpaRepository);
    }

    @Override
    public Optional<Customer> customerOfId(final CustomerId anId) {
        Objects.requireNonNull(anId, "Id cannot be null");
        return this.customerJpaRepository.findById(UUID.fromString(anId.value()))
                .map(CustomerEntity::toDomain);
    }

    @Override
    public Optional<Customer> customerOfCpf(final Cpf aCpf) {
        Objects.requireNonNull(aCpf, "Cpf cannot be null");
        return this.customerJpaRepository.findByCpf(aCpf.value())
                .map(CustomerEntity::toDomain);
    }

    @Override
    public Optional<Customer> customerOfEmail(final Email anEmail) {
        Objects.requireNonNull(anEmail, "Email cannot be null");
        return this.customerJpaRepository.findByEmail(anEmail.value())
                .map(CustomerEntity::toDomain);
    }

    @Transactional
    @Override
    public Customer create(final Customer aCustomer) {
        return this.customerJpaRepository.save(CustomerEntity.of(aCustomer))
                .toDomain();
    }

    @Transactional
    @Override
    public Customer update(final Customer aCustomer) {
        return this.customerJpaRepository.save(CustomerEntity.of(aCustomer))
                .toDomain();
    }

    @Override
    public void deleteAll() {
        this.customerJpaRepository.deleteAll();
    }
}
