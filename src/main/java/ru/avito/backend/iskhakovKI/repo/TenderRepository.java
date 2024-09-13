package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Tender;
import ru.avito.backend.iskhakovKI.domain.Organization;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenderRepository extends JpaRepository<Tender, Long> {
    List<Tender> findByCreator(Employee creator);
    List<Tender> findByOrganization(Organization organization);
}
