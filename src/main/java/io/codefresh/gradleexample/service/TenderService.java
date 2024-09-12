package io.codefresh.gradleexample.service;

import io.codefresh.gradleexample.domain.Tender;
import io.codefresh.gradleexample.repo.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Configuration
public class TenderService {

    @Autowired
    private TenderRepository tenderRepository;

    public Tender createTender(Tender tender) {
        return tenderRepository.save(tender);
    }

    public List<Tender> getAllTenders() {
        return tenderRepository.findAll();
    }

    public List<Tender> getTendersByUser(String username) {
        return tenderRepository.findByCreatorUsername(username);
    }

    public Optional<Tender> getTenderById(Long id) {
        return tenderRepository.findById(id);
    }


    public void deleteTender(Long id) {
        tenderRepository.deleteById(id);
    }

    public Tender updateTender(Long tenderId, Tender tenderDetails) {
        return tenderRepository.save(tenderDetails);
    }
}
