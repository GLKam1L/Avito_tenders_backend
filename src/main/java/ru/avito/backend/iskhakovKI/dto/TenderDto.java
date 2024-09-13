package ru.avito.backend.iskhakovKI.dto;

import lombok.Data;

@Data
public class TenderDto {
    private Long id;
    private String name;
    private String description;
    private String serviceType;
    private String status;
    private Long organizationId;
    private String creatorUsername;
    private int version;
}

