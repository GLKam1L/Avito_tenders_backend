package ru.avito.backend.iskhakovKI.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bid_version")
public class BidVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int version;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    private String name;
    private String description;
}
