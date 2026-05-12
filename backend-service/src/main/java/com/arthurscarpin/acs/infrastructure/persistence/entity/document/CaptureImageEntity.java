package com.arthurscarpin.acs.infrastructure.persistence.entity.document;

import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptureImageEntity {

    private String id;

    private String filename;

    private ImageStatus status;

    private List<CaptureOCREntity> ocr;

    private Instant timestamp;
}
