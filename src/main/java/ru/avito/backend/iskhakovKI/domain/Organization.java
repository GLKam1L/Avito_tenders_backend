package ru.avito.backend.iskhakovKI.domain;

import ru.avito.backend.iskhakovKI.enums.OrganizationType;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "organization_type")
    private OrganizationType type;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    // Связь с ответственными лицами
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<OrganizationResponsible> responsibles;
}



