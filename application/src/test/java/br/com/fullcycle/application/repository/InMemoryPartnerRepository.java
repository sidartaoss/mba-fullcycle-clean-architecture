package br.com.fullcycle.application.repository;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.partner.PartnerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partners;
    private final Map<String, Partner> partnersByCnpj;

    public InMemoryPartnerRepository() {
        this.partners = new HashMap<>();
        this.partnersByCnpj = new HashMap<>();
        this.partnersByEmail = new HashMap<>();
    }

    private final Map<String, Partner> partnersByEmail;

    @Override
    public Optional<Partner> partnerOfId(PartnerId anId) {
        return Optional.ofNullable(this.partners.get(Objects.requireNonNull(anId).value()));
    }

    @Override
    public Optional<Partner> partnerOfCnpj(Cnpj aCnpj) {
        return Optional.ofNullable(this.partnersByCnpj.get(Objects.requireNonNull(aCnpj).value()));
    }

    @Override
    public Optional<Partner> partnerOfEmail(Email anEmail) {
        return Optional.ofNullable(this.partnersByEmail.get(Objects.requireNonNull(anEmail).value()));
    }

    @Override
    public Partner create(final Partner partner) {
        this.partners.put(partner.partnerId().value(), partner);
        this.partnersByCnpj.put(partner.cnpj().value(), partner);
        this.partnersByEmail.put(partner.email().value(), partner);
        return partner;
    }

    @Override
    public Partner update(final Partner partner) {
        this.partners.put(partner.partnerId().value(), partner);
        this.partnersByCnpj.put(partner.cnpj().value(), partner);
        this.partnersByEmail.put(partner.email().value(), partner);
        return partner;
    }

    @Override
    public void deleteAll() {
        this.partners.clear();
        this.partnersByCnpj.clear();
        this.partnersByEmail.clear();
    }
}
