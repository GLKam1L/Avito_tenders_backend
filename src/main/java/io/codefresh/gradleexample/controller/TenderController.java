package io.codefresh.gradleexample.controller;

import io.codefresh.gradleexample.domain.Tender;
import io.codefresh.gradleexample.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {

    @Autowired
    private TenderService tenderService;

    @PostMapping("/new")
    public ResponseEntity<Tender> createTender(@RequestBody Tender tender) {
        Tender createdTender = tenderService.createTender(tender);
        return new ResponseEntity<>(createdTender, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tender>> getTenders() {
        return new ResponseEntity<>(tenderService.getAllTenders(), HttpStatus.OK);
    }

    @GetMapping("/{tenderId}")
    public ResponseEntity<Tender> getTenderById(@PathVariable Long tenderId) {
        return tenderService.getTenderById(tenderId)
                .map(tender -> new ResponseEntity<>(tender, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{tenderId}/edit")
    public ResponseEntity<Tender> updateTender(@PathVariable Long tenderId, @RequestBody Tender tenderDetails) {
        return new ResponseEntity<>(tenderService.updateTender(tenderId, tenderDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{tenderId}/delete")
    public ResponseEntity<Void> deleteTender(@PathVariable Long tenderId) {
        tenderService.deleteTender(tenderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
