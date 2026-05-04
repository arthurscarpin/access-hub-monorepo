package com.arthurscarpin.acs.core.owner.usecase;

import com.arthurscarpin.acs.core.owner.domain.DocumentFactory;
import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.exception.DocumentDuplicateException;
import com.arthurscarpin.acs.core.owner.exception.EmailDuplicateException;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterOwnerImplTest {

    @InjectMocks
    private RegisterOwnerImpl useCase;

    @Mock
    private OwnerGateway gateway;

    @Test
    @DisplayName("Given valid owner, when registering, then should save successfully")
    void shouldRegisterOwnerSuccessfully() {
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );
        var document = DocumentFactory.create(owner.documentType(), owner.document());

        when(gateway.findByDocumentAndDocumentType(document.value(), owner.documentType())).thenReturn(Optional.empty());
        when(gateway.findByEmail(owner.email())).thenReturn(Optional.empty());
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Owner response = useCase.execute(owner);

        assertEquals(owner.name(), response.name());
        assertEquals(owner.email(), response.email());
        assertEquals(document.value(), response.document());
        verify(gateway).save(any(Owner.class));
    }

    @Test
    @DisplayName("Given owner with existing document, when registering, then should throw DocumentDuplicateException")
    void shouldThrowExceptionWhenDocumentAlreadyExists() {
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );
        var document = DocumentFactory.create(owner.documentType(), owner.document());

        when(gateway.findByDocumentAndDocumentType(document.value(), owner.documentType())).thenReturn(Optional.of(owner));

        assertThrows(DocumentDuplicateException.class, () -> useCase.execute(owner));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("Given owner with existing email, when registering, then should throw EmailDuplicateException")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );
        var document = DocumentFactory.create(owner.documentType(), owner.document());

        when(gateway.findByDocumentAndDocumentType(document.value(), owner.documentType())).thenReturn(Optional.empty());
        when(gateway.findByEmail(owner.email())).thenReturn(Optional.of(owner));

        assertThrows(EmailDuplicateException.class, () -> useCase.execute(owner));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("Given valid owner, when registering, then should normalize document before saving")
    void shouldNormalizeDocumentBeforeSaving() {
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );
        var document = DocumentFactory.create(owner.documentType(), owner.document());

        when(gateway.findByDocumentAndDocumentType(document.value(), owner.documentType())).thenReturn(Optional.empty());
        when(gateway.findByEmail(owner.email())).thenReturn(Optional.empty());
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        useCase.execute(owner);

        verify(gateway).save(argThat(saved -> saved.document().equals(document.value())));
    }

    @Test
    @DisplayName("Given gateway failure, when registering, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );
        when(gateway.findByDocumentAndDocumentType(any(), any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(owner));
        verify(gateway, never()).save(any());
    }
}