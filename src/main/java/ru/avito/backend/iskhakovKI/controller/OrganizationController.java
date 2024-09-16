package ru.avito.backend.iskhakovKI.controller;

import ru.avito.backend.iskhakovKI.dto.OrganizationDto;
import ru.avito.backend.iskhakovKI.dto.OrganizationResponsibleDto;
import ru.avito.backend.iskhakovKI.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления организациями
 */
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    // Создание новой организации
    @PostMapping("/new")
    public ResponseEntity<OrganizationDto> createOrganization(@RequestBody OrganizationDto organizationDto) {
        OrganizationDto createdOrganization = organizationService.createOrganization(organizationDto);
        return ResponseEntity.ok(createdOrganization);
    }

    @PostMapping("/assign-responsible")
    public ResponseEntity<OrganizationResponsibleDto> assignResponsible(@RequestBody OrganizationResponsibleDto dto) {
        OrganizationResponsibleDto assignedResponsible = organizationService.assignResponsible(dto);
        return ResponseEntity.ok(assignedResponsible);
    }
    // Другие методы (например, получение списка организаций) можно добавить по мере необходимости
}
