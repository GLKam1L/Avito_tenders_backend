package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Organization;
import ru.avito.backend.iskhakovKI.domain.OrganizationResponsible;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationResponsibleRepository extends JpaRepository<OrganizationResponsible, Long> {
    boolean existsByOrganizationAndUser(Organization organization, Employee user);
    long countByOrganization(Organization organization);
}
