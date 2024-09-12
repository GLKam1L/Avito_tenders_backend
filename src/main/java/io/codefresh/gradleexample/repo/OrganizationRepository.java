package io.codefresh.gradleexample.repo;

import io.codefresh.gradleexample.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
