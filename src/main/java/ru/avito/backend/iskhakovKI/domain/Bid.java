package ru.avito.backend.iskhakovKI.domain;

import ru.avito.backend.iskhakovKI.enums.BidStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "bid")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private BidStatus status;

    private int version = 1;

    @ManyToOne
    @JoinColumn(name = "tender_id")
    private Tender tender;

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
    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL)
    private List<BidVersion> versions;

    // Отзывы
    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL)
    private List<Review> reviews;
}



