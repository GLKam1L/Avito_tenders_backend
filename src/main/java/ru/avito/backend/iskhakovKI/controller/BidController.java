package ru.avito.backend.iskhakovKI.controller;


import ru.avito.backend.iskhakovKI.domain.Bid;
import ru.avito.backend.iskhakovKI.dto.BidDto;
import ru.avito.backend.iskhakovKI.dto.ReviewDto;
import ru.avito.backend.iskhakovKI.enums.BidStatus;
import ru.avito.backend.iskhakovKI.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping()
    public ResponseEntity<List<BidDto>> getAllBids() {
        List<BidDto> tenders = bidService.getAllBids();
        return ResponseEntity.ok(tenders);
    }

    // Создание нового предложения
    @PostMapping("/new")
    public ResponseEntity<BidDto> createBid(@RequestBody BidDto bidDto) {
        BidDto createdBid = bidService.createBid(bidDto);
        return ResponseEntity.ok(createdBid);
    }

    // Получение предложений пользователя
    @GetMapping("/my")
    public ResponseEntity<List<BidDto>> getMyBids(@RequestParam String username) {
        List<BidDto> bids = bidService.getBidsByUsername(username);
        return ResponseEntity.ok(bids);
    }

    // Получение предложений для тендера
    @GetMapping("/{tenderId}/list")
    public ResponseEntity<List<BidDto>> getBidsForTender(@PathVariable Long tenderId) {
        List<BidDto> bids = bidService.getBidsByTenderId(tenderId);
        return ResponseEntity.ok(bids);
    }

    // Редактирование предложения
    @PatchMapping("/{bidId}/edit")
    public ResponseEntity<BidDto> editBid(@PathVariable Long bidId, @RequestBody BidDto bidDto) {
        BidDto updatedBid = bidService.editBid(bidId, bidDto);
        return ResponseEntity.ok(updatedBid);
    }

    // Откат версии предложения
    @PutMapping("/{bidId}/rollback/{version}")
    public ResponseEntity<BidDto> rollbackBid(@PathVariable Long bidId, @PathVariable int version) {
        BidDto rolledBackBid = bidService.rollbackBid(bidId, version);
        return ResponseEntity.ok(rolledBackBid);
    }

    // Публикация предложения
    @PostMapping("/{bidId}/publish")
    public ResponseEntity<BidDto> publishBid(@PathVariable Long bidId) {
        BidDto publishedBid = bidService.publishBid(bidId);
        return ResponseEntity.ok(publishedBid);
    }

    // Отмена предложения
    @PostMapping("/{bidId}/cancel")
    public ResponseEntity<BidDto> cancelBid(@PathVariable Long bidId) {
        BidDto canceledBid = bidService.cancelBid(bidId);
        return ResponseEntity.ok(canceledBid);
    }

    // Согласование предложения
    @PostMapping("/{bidId}/approve")
    public ResponseEntity<String> approveBid(@PathVariable Long bidId, @RequestParam String username) {
        String result = bidService.approveBid(bidId, username);
        return ResponseEntity.ok(result);
    }

    // Отклонение предложения
    @PostMapping("/{bidId}/reject")
    public ResponseEntity<String> rejectBid(@PathVariable Long bidId, @RequestParam String username) {
        String result = bidService.rejectBid(bidId, username);
        return ResponseEntity.ok(result);
    }

    // Просмотр отзывов на прошлые предложения
    @GetMapping("/{tenderId}/reviews")
    public ResponseEntity<List<ReviewDto>> getBidReviews(
            @PathVariable Long tenderId,
            @RequestParam String authorUsername,
            @RequestParam Long organizationId
    ) {
        List<ReviewDto> reviews = bidService.getReviews(tenderId, authorUsername, organizationId);
        return ResponseEntity.ok(reviews);
    }

    // Просмотр отзывов на прошлые предложения
    @GetMapping("/{tenderId}/feedback")
    public ResponseEntity<List<ReviewDto>> getBidFeedback(
            @PathVariable Long tenderId,
            @RequestParam String authorUsername,
            @RequestParam Long organizationId
    ) {
        List<ReviewDto> reviews = bidService.getReviews(tenderId, authorUsername, organizationId);
        return ResponseEntity.ok(reviews);
    }

    // Просмотр статуса
    @GetMapping("/{bidId}/status")
    public ResponseEntity<String> getBidStatus(@PathVariable Long bidId) {
        Optional<Bid> optionalBid = bidService.getTenderById(bidId);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();
            BidStatus status = bid.getStatus();
            return new ResponseEntity<>(status.name(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Добавление отзыва
    @PostMapping("/{bidId}/review")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long bidId, @RequestBody ReviewDto reviewDto) {
        ReviewDto addedReview = bidService.addReview(bidId, reviewDto);
        return ResponseEntity.ok(addedReview);
    }

}

