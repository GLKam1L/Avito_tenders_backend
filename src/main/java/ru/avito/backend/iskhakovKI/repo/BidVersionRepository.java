package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.BidVersion;
import ru.avito.backend.iskhakovKI.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidVersionRepository extends JpaRepository<BidVersion, Long> {
    Optional<BidVersion> findByBidAndVersion(Bid bid, int version);
}