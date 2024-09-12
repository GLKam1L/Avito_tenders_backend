package io.codefresh.gradleexample.repo;


import io.codefresh.gradleexample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}