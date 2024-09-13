package ru.avito.backend.iskhakovKI.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "organization_responsible")
public class OrganizationResponsible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Employee user;
}

