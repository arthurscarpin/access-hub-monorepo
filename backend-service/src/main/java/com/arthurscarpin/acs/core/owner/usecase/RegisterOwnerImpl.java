package com.arthurscarpin.acs.core.owner.usecase;

import com.arthurscarpin.acs.core.owner.domain.DocumentFactory;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.owner.exception.DocumentDuplicateException;
import com.arthurscarpin.acs.core.owner.exception.EmailDuplicateException;

public class RegisterOwnerImpl implements RegisterOwnerUseCase {

    private final OwnerGateway gateway;

    public RegisterOwnerImpl(OwnerGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Owner execute(Owner owner) {
        var document = DocumentFactory.create(owner.documentType(), owner.document());

        if (gateway.findByDocumentAndDocumentType(document.value(), owner.documentType()).isPresent()) {
            throw new DocumentDuplicateException("Document already exists");
        }

        if(gateway.findByEmail(owner.email()).isPresent()) {
            throw new EmailDuplicateException("Email already exists");
        }

        Owner ownerSaved = new Owner(
                owner.id(),
                owner.name(),
                document.value(),
                owner.documentType(),
                owner.email()
        );
        return gateway.save(ownerSaved);
    }
}
