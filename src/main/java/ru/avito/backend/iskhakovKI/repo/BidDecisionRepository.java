package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Bid;
import ru.avito.backend.iskhakovKI.domain.BidDecision;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidDecisionRepository extends JpaRepository<BidDecision, Long> {
    boolean existsByBidAndEmployee(Bid bid, Employee employee);
    long countByBidAndApproved(Bid bid, boolean approved);
}