package ru.avito.backend.iskhakovKI.domain;

import ru.avito.backend.iskhakovKI.enums.TenderStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "tender")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String serviceType;

    @Enumerated(EnumType.STRING)
    private TenderStatus status;

    private int version = 1;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Employee creator;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    // История версий
    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL)
    private List<TenderVersion> versions;
}



