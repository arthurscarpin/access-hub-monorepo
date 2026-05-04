package com.arthurscarpin.acs.infrastructure.persistence.repository;

import com.arthurscarpin.acs.infrastructure.persistence.entity.AccessEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccessEventRepository extends
        JpaRepository<AccessEventEntity, UUID>,
        JpaSpecificationExecutor<AccessEventEntity> {
}
