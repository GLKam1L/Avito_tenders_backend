package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);
}
