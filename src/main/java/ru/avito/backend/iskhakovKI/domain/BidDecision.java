package ru.avito.backend.iskhakovKI.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bid_decision")
public class BidDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}

