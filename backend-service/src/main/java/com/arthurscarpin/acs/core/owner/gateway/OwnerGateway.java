package com.arthurscarpin.acs.core.owner.gateway;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;

import java.util.Optional;
import java.util.UUID;

public interface OwnerGateway {

    Optional<Owner> findById(UUID uuid);

    Optional<Owner> findByEmail(String email);

    Optional<Owner> findByDocumentAndDocumentType(String document, DocumentType documentType);

    Owner save(Owner ownerSaved);


}
