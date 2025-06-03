package com.themistech.dasntscam.responses;

import com.themistech.dasntscam.dto.VideoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class VideoListResponse {
    private List<VideoDTO> videoList;
}
