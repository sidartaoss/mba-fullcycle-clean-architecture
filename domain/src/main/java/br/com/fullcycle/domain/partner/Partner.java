package br.com.fullcycle.domain.partner;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.person.Name;

import java.util.Objects;

public class Partner {

    private final PartnerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    private Partner(
            final PartnerId aPartnerId,
            final String aName,
            final String aCnpj,
            final String anEmail) {
        if (aPartnerId == null) {
            throw new ValidationException("Invalid partnerId for Partner");
        }
        this.partnerId = aPartnerId;
        this.changeName(aName);
        this.changeCnpj(aCnpj);
        this.changeEmail(anEmail);
    }

    public static Partner newPartner(
            final String aName,
            final String aCnpj,
            final String anEmail
    ) {
        return new Partner(PartnerId.unique(), aName, aCnpj, anEmail);
    }

    public static Partner with(
            final String anId,
            final String aName,
            final String aCnpj,
            final String anEmail
    ) {
        return new Partner(PartnerId.with(anId), aName, aCnpj, anEmail);
    }

    public PartnerId partnerId() {
        return partnerId;
    }

    public Name name() {
        return name;
    }

    public Cnpj cnpj() {
        return cnpj;
    }

    public Email email() {
        return email;
    }

    private void changeName(final String aName) {
        this.name = new Name(aName);
    }

    private void changeCnpj(final String aCnpj) {
        this.cnpj = new Cnpj(aCnpj);
    }

    private void changeEmail(final String anEmail) {
        this.email = new Email(anEmail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partner partner = (Partner) o;
        return Objects.equals(partnerId, partner.partnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerId);
    }
}
