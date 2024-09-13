package ru.avito.backend.iskhakovKI.service;


import ru.avito.backend.iskhakovKI.domain.Employee;
import ru.avito.backend.iskhakovKI.domain.Organization;
import ru.avito.backend.iskhakovKI.domain.Tender;
import ru.avito.backend.iskhakovKI.domain.TenderVersion;
import ru.avito.backend.iskhakovKI.dto.TenderDto;
import ru.avito.backend.iskhakovKI.enums.TenderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.avito.backend.iskhakovKI.repo.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenderService {

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationResponsibleRepository organizationResponsibleRepository;

    @Autowired
    private TenderVersionRepository tenderVersionRepository;

    // Получение всех тендеров
    public List<TenderDto> getAllTenders() {
        List<Tender> tenders = tenderRepository.findAll();
        return tenders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Создание нового тендера
    public TenderDto createTender(TenderDto tenderDto) {
        Employee creator = employeeRepository.findByUsername(tenderDto.getCreatorUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Organization organization = organizationRepository.findById(tenderDto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Проверка, что пользователь является ответственным за организацию
        if (!organizationResponsibleRepository.existsByOrganizationAndUser(organization, creator)) {
            throw new RuntimeException("User is not responsible for the organization");
        }

        Tender tender = new Tender();
        tender.setName(tenderDto.getName());
        tender.setDescription(tenderDto.getDescription());
        tender.setServiceType(tenderDto.getServiceType());
        tender.setStatus(TenderStatus.CREATED);
        tender.setOrganization(organization);
        tender.setCreator(creator);

        tenderRepository.save(tender);

        // Сохранение версии
        saveTenderVersion(tender);

        return convertToDto(tender);
    }

    // Получение тендеров пользователя
    public List<TenderDto> getTendersByUsername(String username) {
        Employee user = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Tender> tenders = tenderRepository.findByCreator(user);
        return tenders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Редактирование тендера
    public TenderDto editTender(Long tenderId, TenderDto tenderDto) {
        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        tender.setName(tenderDto.getName());
        tender.setDescription(tenderDto.getDescription());
        tender.setVersion(tender.getVersion() + 1);

        tenderRepository.save(tender);

        // Сохранение новой версии
        saveTenderVersion(tender);

        return convertToDto(tender);
    }

    // Откат версии тендера
    public TenderDto rollbackTender(Long tenderId, int version) {
        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        TenderVersion tenderVersion = tenderVersionRepository.findByTenderAndVersion(tender, version)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        tender.setName(tenderVersion.getName());
        tender.setDescription(tenderVersion.getDescription());
        tender.setServiceType(tenderVersion.getServiceType());
        tender.setVersion(tender.getVersion() + 1);

        tenderRepository.save(tender);

        // Сохранение новой версии
        saveTenderVersion(tender);

        return convertToDto(tender);
    }

    // Метод для сохранения версии тендера
    private void saveTenderVersion(Tender tender) {
        TenderVersion version = new TenderVersion();
        version.setTender(tender);
        version.setVersion(tender.getVersion());
        version.setName(tender.getName());
        version.setDescription(tender.getDescription());
        version.setServiceType(tender.getServiceType());

        tenderVersionRepository.save(version);
    }

    // Преобразование в DTO
    private TenderDto convertToDto(Tender tender) {
        TenderDto dto = new TenderDto();
        dto.setId(tender.getId());
        dto.setName(tender.getName());
        dto.setDescription(tender.getDescription());
        dto.setServiceType(tender.getServiceType());
        dto.setStatus(tender.getStatus().name());
        dto.setOrganizationId(tender.getOrganization().getId());
        dto.setCreatorUsername(tender.getCreator().getUsername());
        dto.setVersion(tender.getVersion());
        return dto;
    }

    // Публикация тендера
    public TenderDto publishTender(Long tenderId) {
        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        tender.setStatus(TenderStatus.PUBLISHED);
        tenderRepository.save(tender);

        return convertToDto(tender);
    }

    // Закрытие тендера
    public TenderDto closeTender(Long tenderId) {
        Tender tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new RuntimeException("Tender not found"));

        tender.setStatus(TenderStatus.CLOSED);
        tenderRepository.save(tender);

        return convertToDto(tender);
    }

    public Optional<Tender> getTenderById(Long tenderId) {
        return tenderRepository.findById(tenderId);
    }
}

