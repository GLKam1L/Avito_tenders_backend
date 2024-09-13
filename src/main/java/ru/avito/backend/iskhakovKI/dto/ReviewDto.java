package ru.avito.backend.iskhakovKI.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String comment;
    private Long bidId;
    private String authorUsername;
}