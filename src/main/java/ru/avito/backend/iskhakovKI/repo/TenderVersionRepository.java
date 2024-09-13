package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.TenderVersion;
import ru.avito.backend.iskhakovKI.domain.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenderVersionRepository extends JpaRepository<TenderVersion, Long> {
    Optional<TenderVersion> findByTenderAndVersion(Tender tender, int version);
}
