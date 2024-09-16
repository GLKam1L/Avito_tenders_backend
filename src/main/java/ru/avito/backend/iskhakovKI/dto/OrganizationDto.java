package ru.avito.backend.iskhakovKI.dto;

import ru.avito.backend.iskhakovKI.enums.OrganizationType;
import lombok.Data;

@Data
public class OrganizationDto {
    private Long id;
    private String name;
    private String description;
    private OrganizationType type;
}

