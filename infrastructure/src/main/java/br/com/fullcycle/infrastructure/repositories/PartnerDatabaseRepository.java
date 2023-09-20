package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class PartnerDatabaseRepository implements PartnerRepository {

    private final PartnerJpaRepository partnerJpaRepository;

    public PartnerDatabaseRepository(final PartnerJpaRepository partnerJpaRepository) {
        this.partnerJpaRepository = Objects.requireNonNull(partnerJpaRepository);
    }

    @Override
    public Optional<Partner> partnerOfId(final PartnerId anId) {
        Objects.requireNonNull(anId, "Id cannot be null");
        return this.partnerJpaRepository.findById(UUID.fromString(anId.value()))
                .map(PartnerEntity::toDomain);
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final Cnpj aCnpj) {
        Objects.requireNonNull(aCnpj, "Cnpj cannot be null");
        return this.partnerJpaRepository.findByCnpj(aCnpj.value())
                .map(PartnerEntity::toDomain);
    }

    @Override
    public Optional<Partner> partnerOfEmail(final Email anEmail) {
        Objects.requireNonNull(anEmail, "Email cannot be null");
        return this.partnerJpaRepository.findByEmail(anEmail.value())
                .map(PartnerEntity::toDomain);
    }

    @Transactional
    @Override
    public Partner create(final Partner aPartner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(aPartner))
                .toDomain();
    }

    @Transactional
    @Override
    public Partner update(final Partner aPartner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(aPartner))
                .toDomain();
    }

    @Override
    public void deleteAll() {
        this.partnerJpaRepository.deleteAll();
    }
}
