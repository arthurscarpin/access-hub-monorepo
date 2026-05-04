package com.arthurscarpin.acs.infrastructure.persistence.repository;

import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScopeRepository extends JpaRepository<ScopeEntity, UUID> {

    @Query("SELECT s.id FROM ScopeEntity s WHERE s.id IN :ids")
    List<UUID> findAllIdsByIds(@Param("ids") List<UUID> ids);

    List<ScopeEntity> findAllByIdIn(List<UUID> ids);
}
