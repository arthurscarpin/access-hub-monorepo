package com.arthurscarpin.acs.infrastructure.persistence.entity.document;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptureOCREntity {
    private String text;

    private Double confidence;

    private List<List<Integer>> bbox;
}
