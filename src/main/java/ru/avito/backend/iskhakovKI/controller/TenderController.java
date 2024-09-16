package ru.avito.backend.iskhakovKI.controller;

import ru.avito.backend.iskhakovKI.domain.Tender;
import ru.avito.backend.iskhakovKI.dto.TenderDto;
import ru.avito.backend.iskhakovKI.enums.TenderStatus;
import ru.avito.backend.iskhakovKI.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {

    @Autowired
    private TenderService tenderService;

    // Получение статуса тендера
    @GetMapping("/{tenderId}/status")
    public ResponseEntity<String> getTenderStatus(@PathVariable Long tenderId) {
        Optional<Tender> optionalTender = tenderService.getTenderById(tenderId);
        if (optionalTender.isPresent()) {
            Tender tender = optionalTender.get();
            TenderStatus status = tender.getStatus();
            return new ResponseEntity<>(status.name(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Получение списка тендеров
    @GetMapping()
    public ResponseEntity<List<TenderDto>> getAllTenders() {
        List<TenderDto> tenders = tenderService.getAllTenders();
        return ResponseEntity.ok(tenders);
    }

    // Создание нового тендера
    @PostMapping("/new")
    public ResponseEntity<TenderDto> createTender(@RequestBody TenderDto tenderDto) {
        TenderDto createdTender = tenderService.createTender(tenderDto);
        return ResponseEntity.ok(createdTender);
    }

    // Получение тендеров пользователя
    @GetMapping("/my")
    public ResponseEntity<List<TenderDto>> getMyTenders(@RequestParam String username) {
        List<TenderDto> tenders = tenderService.getTendersByUsername(username);
        return ResponseEntity.ok(tenders);
    }

    // Редактирование тендера
    @PatchMapping("/{tenderId}/edit")
    public ResponseEntity<TenderDto> editTender(@PathVariable Long tenderId, @RequestBody TenderDto tenderDto) {
        TenderDto updatedTender = tenderService.editTender(tenderId, tenderDto);
        return ResponseEntity.ok(updatedTender);
    }

    // Откат версии тендера
    @PutMapping("/{tenderId}/rollback/{version}")
    public ResponseEntity<TenderDto> rollbackTender(@PathVariable Long tenderId, @PathVariable int version) {
        TenderDto rolledBackTender = tenderService.rollbackTender(tenderId, version);
        return ResponseEntity.ok(rolledBackTender);
    }


    // Публикация тендера
    @PostMapping("/{tenderId}/publish")
    public ResponseEntity<TenderDto> publishTender(@PathVariable Long tenderId) {
        TenderDto publishedTender = tenderService.publishTender(tenderId);
        return ResponseEntity.ok(publishedTender);
    }

    // Закрытие тендера
    @PostMapping("/{tenderId}/close")
    public ResponseEntity<TenderDto> closeTender(@PathVariable Long tenderId) {
        TenderDto closedTender = tenderService.closeTender(tenderId);
        return ResponseEntity.ok(closedTender);
    }
}


