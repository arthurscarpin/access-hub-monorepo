package com.arthurscarpin.acs.infrastructure.persistence.repository;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, UUID> {

    Optional<OwnerEntity> findByEmail(String email);

    Optional<OwnerEntity> findByDocumentAndDocumentType(String document, DocumentType documentType);
}
