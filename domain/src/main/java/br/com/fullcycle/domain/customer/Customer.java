package br.com.fullcycle.domain.customer;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.person.Name;

import java.util.Objects;

public class Customer {

    private final CustomerId customerId;
    private Name name;
    private Cpf cpf;
    private Email email;

    private Customer(
            final CustomerId aCustomerId,
            final String aName,
            final String aCpf,
            final String anEmail) {
        if (aCustomerId == null) {
            throw new ValidationException("Invalid customerId for Customer");
        }
        this.customerId = aCustomerId;
        this.changeName(aName);
        this.changeCpf(aCpf);
        this.changeEmail(anEmail);
    }

    public static Customer newCustomer(
            final String aName,
            final String aCpf,
            final String anEmail
    ) {
        return new Customer(CustomerId.unique(), aName, aCpf, anEmail);
    }

    public static Customer with(
            final String anId,
            final String aName,
            final String aCpf,
            final String anEmail) {
        return new Customer(CustomerId.with(anId), aName, aCpf, anEmail);
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Name name() {
        return name;
    }

    public Cpf cpf() {
        return cpf;
    }

    public Email email() {
        return email;
    }

    private void changeCpf(final String aCpf) {
        this.cpf = new Cpf(aCpf);
    }

    private void changeEmail(final String anEmail) {
        this.email = new Email(anEmail);
    }

    private void changeName(final String aName) {
        this.name = new Name(aName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
