package io.codefresh.gradleexample.repo;

import io.codefresh.gradleexample.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByTenderId(Long tenderId);
    List<Bid> findByCreatorUsername(String username);
}
