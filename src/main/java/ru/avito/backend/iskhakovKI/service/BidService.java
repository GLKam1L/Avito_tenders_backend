package ru.avito.backend.iskhakovKI.service;

import ru.avito.backend.iskhakovKI.domain.*;
import ru.avito.backend.iskhakovKI.dto.BidDto;
import ru.avito.backend.iskhakovKI.dto.ReviewDto;
import ru.avito.backend.iskhakovKI.enums.BidStatus;
import ru.avito.backend.iskhakovKI.enums.TenderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.avito.backend.iskhakovKI.repo.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BidVersionRepository bidVersionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrganizationResponsibleRepository organizationResponsibleRepository;

    @Autowired
    private BidDecisionRepository bidDecisionRepository;


    public List<BidDto> getAllBids() {
        List<Bid> bids = bidRepository.findAll();
        return bids.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Создание нового предложения
    public BidDto createBid(BidDto bidDto) {
        Employee creator = employeeRepository.findByUsername(bidDto.getCreatorUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tender tender = tenderRepository.findById(bidDto.getTenderId())
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        Organization organization = organizationRepository.findById(bidDto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Bid bid = new Bid();
        bid.setName(bidDto.getName());
        bid.setDescription(bidDto.getDescription());
        bid.setStatus(BidStatus.CREATED);
        bid.setTender(tender);
        bid.setOrganization(organization);
        bid.setCreator(creator);

        bidRepository.save(bid);

        // Сохранение версии
        saveBidVersion(bid);

        return convertToDto(bid);
    }

    // Получение предложений пользователя
    public List<BidDto> getBidsByUsername(String username) {
        Employee user = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bid> bids = bidRepository.findByCreator(user);
        return bids.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Получение предложений для тендера
    public List<BidDto> getBidsByTenderId(Long tenderId) {
        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        List<Bid> bids = bidRepository.findByTender(tender);
        return bids.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Редактирование предложения
    public BidDto editBid(Long bidId, BidDto bidDto) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        bid.setName(bidDto.getName());
        bid.setDescription(bidDto.getDescription());
        bid.setVersion(bid.getVersion() + 1);

        bidRepository.save(bid);

        // Сохранение новой версии
        saveBidVersion(bid);

        return convertToDto(bid);
    }

    // Откат версии предложения
    public BidDto rollbackBid(Long bidId, int version) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        BidVersion bidVersion = bidVersionRepository.findByBidAndVersion(bid, version)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        bid.setName(bidVersion.getName());
        bid.setDescription(bidVersion.getDescription());
        bid.setVersion(bid.getVersion() + 1);

        bidRepository.save(bid);

        // Сохранение новой версии
        saveBidVersion(bid);

        return convertToDto(bid);
    }

    // Метод для сохранения версии предложения
    private void saveBidVersion(Bid bid) {
        BidVersion version = new BidVersion();
        version.setBid(bid);
        version.setVersion(bid.getVersion());
        version.setName(bid.getName());
        version.setDescription(bid.getDescription());

        bidVersionRepository.save(version);
    }

    // Просмотр отзывов на прошлые предложения
    public List<ReviewDto> getReviews(Long tenderId, String authorUsername, Long organizationId) {
        Employee author = employeeRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Проверка, что пользователь является ответственным за организацию
        if (!organizationResponsibleRepository.existsByOrganizationAndUser(organization, author)) {
            throw new RuntimeException("User is not responsible for the organization");
        }

        List<Bid> bids = bidRepository.findByTender(tender);

        List<Review> reviews = bids.stream()
                .flatMap(bid -> reviewRepository.findByBidAndAuthor(bid, author).stream())
                .collect(Collectors.toList());

        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Преобразование Bid в DTO
    private BidDto convertToDto(Bid bid) {
        BidDto dto = new BidDto();
        dto.setId(bid.getId());
        dto.setName(bid.getName());
        dto.setDescription(bid.getDescription());
        dto.setStatus(bid.getStatus().name());
        dto.setTenderId(bid.getTender().getId());
        dto.setOrganizationId(bid.getOrganization().getId());
        dto.setCreatorUsername(bid.getCreator().getUsername());
        dto.setVersion(bid.getVersion());
        return dto;
    }


    public ReviewDto addReview(Long bidId, ReviewDto reviewDto) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        Employee author = employeeRepository.findByUsername(reviewDto.getAuthorUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Review review = new Review();
        review.setBid(bid);
        review.setAuthor(author);
        review.setComment(reviewDto.getComment());

        reviewRepository.save(review);

        return convertToDto(review);
    }



    // Преобразование Review в DTO
    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setBidId(review.getBid().getId());
        dto.setAuthorUsername(review.getAuthor().getUsername());
        return dto;
    }

    // Публикация предложения
    public BidDto publishBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        bid.setStatus(BidStatus.PUBLISHED);
        bidRepository.save(bid);

        return convertToDto(bid);
    }

    // Отмена предложения
    public BidDto cancelBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        bid.setStatus(BidStatus.CANCELED);
        bidRepository.save(bid);

        return convertToDto(bid);
    }

    // Согласование предложения
    public String approveBid(Long bidId, String username) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        Employee approver = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tender tender = bid.getTender();
        Organization organization = tender.getOrganization();

        // Проверка, что пользователь является ответственным за организацию
        if (!organizationResponsibleRepository.existsByOrganizationAndUser(organization, approver)) {
            throw new RuntimeException("User is not responsible for the organization");
        }

        // Проверка, не принял ли пользователь уже решение
        if (bidDecisionRepository.existsByBidAndEmployee(bid, approver)) {
            throw new RuntimeException("User has already made a decision on this bid");
        }

        // Сохранение решения
        BidDecision decision = new BidDecision();
        decision.setBid(bid);
        decision.setEmployee(approver);
        decision.setApproved(true);
        bidDecisionRepository.save(decision);

        // Проверка кворума
        String result = checkQuorum(bid);
        return result;
    }

    // Отклонение предложения
    public String rejectBid(Long bidId, String username) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        Employee rejecter = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tender tender = bid.getTender();
        Organization organization = tender.getOrganization();

        // Проверка, что пользователь является ответственным за организацию
        if (!organizationResponsibleRepository.existsByOrganizationAndUser(organization, rejecter)) {
            throw new RuntimeException("User is not responsible for the organization");
        }

        // Проверка, не принял ли пользователь уже решение
        if (bidDecisionRepository.existsByBidAndEmployee(bid, rejecter)) {
            throw new RuntimeException("User has already made a decision on this bid");
        }

        // Сохранение решения
        BidDecision decision = new BidDecision();
        decision.setBid(bid);
        decision.setEmployee(rejecter);
        decision.setApproved(false);
        bidDecisionRepository.save(decision);

        // Отклонение предложения при наличии rejected
        bid.setStatus(BidStatus.REJECTED);
        bidRepository.save(bid);

        return "Bid has been rejected due to a rejection by one of the responsibles.";
    }

    // Метод для проверки кворума
    private String checkQuorum(Bid bid) {
        Tender tender = bid.getTender();
        Organization organization = tender.getOrganization();

        long totalResponsibles = organizationResponsibleRepository.countByOrganization(organization);
        int quorum = (int) Math.min(3, totalResponsibles);

        long approvals = bidDecisionRepository.countByBidAndApproved(bid, true);

        if (approvals >= quorum) {
            bid.setStatus(BidStatus.AGREED);
            bidRepository.save(bid);

            // Автоматически закрываем тендер
            tender.setStatus(TenderStatus.CLOSED);
            tenderRepository.save(tender);

            return "Bid has been approved and tender has been closed.";
        }

        return "Bid has been approved by one more responsible. Waiting for more approvals.";
    }

    public Optional<Bid> getTenderById(Long bidId) {
        return bidRepository.findById(bidId);
    }

}

