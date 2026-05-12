package com.arthurscarpin.acs.infrastructure.persistence.entity.document;

import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "capture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptureEntity {

    @Id
    private String id;

    private List<CaptureImageEntity> images;

    private CaptureStatus status;

    private String finalPlate;

    private Double finalConfidence;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant processedAt;

    @Builder.Default
    private Integer processedImagesCount = 0;

    @Version
    private Long version;
}
