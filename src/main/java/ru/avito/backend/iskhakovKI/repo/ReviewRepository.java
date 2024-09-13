package ru.avito.backend.iskhakovKI.repo;

import ru.avito.backend.iskhakovKI.domain.Review;
import ru.avito.backend.iskhakovKI.domain.Bid;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBidAndAuthor(Bid bid, Employee author);
}