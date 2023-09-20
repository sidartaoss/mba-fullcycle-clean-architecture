package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.partner.Partner;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity(name = "Partner")
@Table(name = "partners")
public class PartnerEntity {

    @Id
    private UUID id;

    private String name;

    private String cnpj;

    private String email;

    public PartnerEntity() {
    }

    private PartnerEntity(String name, String cnpj, String email) {
        this.name = name;
        this.cnpj = cnpj;
        this.email = email;
    }

    private PartnerEntity(UUID id, String name, String cnpj, String email) {
        this(name, cnpj, email);
        this.id = id;
    }

    public static PartnerEntity with(
            final String name,
            final String cnpj,
            final String email) {
        return new PartnerEntity(name, cnpj, email);
    }

    public static PartnerEntity with(
            final UUID id,
            final String name,
            final String cnpj,
            final String email) {
        return new PartnerEntity(id, name, cnpj, email);
    }

    public static PartnerEntity of(final Partner aPartner) {
        return with(
          UUID.fromString(aPartner.partnerId().value()),
          aPartner.name().value(),
          aPartner.cnpj().value(),
          aPartner.email().value()
        );
    }

    public Partner toDomain() {
        return Partner.with(
                getId().toString(),
                getName(),
                getCnpj(),
                getEmail()
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
