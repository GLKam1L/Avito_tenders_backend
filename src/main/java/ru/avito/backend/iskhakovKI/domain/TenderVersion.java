package ru.avito.backend.iskhakovKI.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "tender_version")
public class TenderVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int version;

    @ManyToOne
    @JoinColumn(name = "tender_id")
    private Tender tender;

    private String name;
    private String description;
    private String serviceType;
}
