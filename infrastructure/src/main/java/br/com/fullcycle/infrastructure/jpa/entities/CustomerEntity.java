package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.customer.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "Customer")
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private UUID id;

    private String name;

    private String cpf;

    private String email;

    public CustomerEntity() {
    }

    private CustomerEntity(String aName, String aCpf, String anEmail) {
        this.name = aName;
        this.cpf = aCpf;
        this.email = anEmail;
    }

    private CustomerEntity(UUID anId, String aName, String aCpf, String anEmail) {
        this(aName, aCpf, anEmail);
        this.id = anId;
    }

    public static CustomerEntity with(final UUID anId, final String aName, final String aCpf, final String anEmail) {
        return new CustomerEntity(anId, aName, aCpf, anEmail);
    }

    public static CustomerEntity with(final String aName, final String aCpf, final String anEmail) {
        return new CustomerEntity(aName, aCpf, anEmail);
    }

    public static CustomerEntity of(final Customer aCustomer) {
        return with(
                UUID.fromString(aCustomer.customerId().value()),
                aCustomer.name().value(),
                aCustomer.cpf().value(),
                aCustomer.email().value()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Customer toDomain() {
        return Customer.with(
                getId().toString(),
                getName(),
                getCpf(),
                getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity customerEntity = (CustomerEntity) o;
        return Objects.equals(id, customerEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
