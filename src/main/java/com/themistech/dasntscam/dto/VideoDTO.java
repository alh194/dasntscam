package com.themistech.dasntscam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class VideoDTO {
    private String id;
    private String imageUrl;
    private String date;
    private String time;
    private String dateTime;
    private boolean isFeatured;
}
