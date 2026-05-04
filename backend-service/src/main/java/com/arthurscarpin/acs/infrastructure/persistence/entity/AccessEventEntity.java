package com.arthurscarpin.acs.infrastructure.persistence.entity;

import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccessEventEntity {

    private static final long serialVersionUUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessResult result;
}
