package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Bid;
import ru.avito.backend.iskhakovKI.domain.Tender;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByCreator(Employee creator);
    List<Bid> findByTender(Tender tender);
}
