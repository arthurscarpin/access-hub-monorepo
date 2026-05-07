package com.arthurscarpin.acs.infrastructure.persistence.entity;

import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptureImageEntity {

    private String id;

    private String filename;

    private String ocrText;

    private Double confidence;

    private ImageStatus status;

    private Instant timestamp;
}
