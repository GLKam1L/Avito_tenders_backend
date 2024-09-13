package ru.avito.backend.iskhakovKI.dto;

import lombok.Data;

@Data
public class BidDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Long tenderId;
    private Long organizationId;
    private String creatorUsername;
    private int version;
}