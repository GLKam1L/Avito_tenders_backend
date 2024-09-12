package io.codefresh.gradleexample.repo;


import io.codefresh.gradleexample.domain.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenderRepository extends JpaRepository<Tender, Long> {
    List<Tender> findByOrganizationId(Long organizationId);
    List<Tender> findByCreatorUsername(String username);
}
