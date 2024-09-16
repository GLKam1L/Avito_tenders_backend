package ru.avito.backend.iskhakovKI.service;

import ru.avito.backend.iskhakovKI.dto.OrganizationDto;
import ru.avito.backend.iskhakovKI.domain.Organization;
import ru.avito.backend.iskhakovKI.dto.OrganizationResponsibleDto;
import ru.avito.backend.iskhakovKI.repo.OrganizationRepository;
import ru.avito.backend.iskhakovKI.repo.OrganizationResponsibleRepository;
import ru.avito.backend.iskhakovKI.repo.EmployeeRepository;
import ru.avito.backend.iskhakovKI.domain.OrganizationResponsible;
import ru.avito.backend.iskhakovKI.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public OrganizationDto createOrganization(OrganizationDto organizationDto) {
        // Проверяем, нет ли организации с таким же именем
        if (organizationRepository.findByName(organizationDto.getName()).isPresent()) {
            throw new RuntimeException("Organization with this name already exists");
        }

        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());
        organization.setType(organizationDto.getType());

        organizationRepository.save(organization);

        organizationDto.setId(organization.getId());
        return organizationDto;
    }

    @Autowired
    private OrganizationResponsibleRepository organizationResponsibleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Метод для назначения ответственного пользователя
    public OrganizationResponsibleDto assignResponsible(OrganizationResponsibleDto dto) {
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Employee user = employeeRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверяем, не является ли пользователь уже ответственным за организацию
        if (organizationResponsibleRepository.existsByOrganizationAndUser(organization, user)) {
            throw new RuntimeException("User is already responsible for this organization");
        }

        OrganizationResponsible responsible = new OrganizationResponsible();
        responsible.setOrganization(organization);
        responsible.setUser(user);

        organizationResponsibleRepository.save(responsible);

        dto.setId(responsible.getId());
        return dto;
    }
}
