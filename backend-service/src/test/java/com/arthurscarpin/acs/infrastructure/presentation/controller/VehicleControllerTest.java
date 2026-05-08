package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.OwnerRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.VehicleRepository;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VehicleControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private UUID ownerId;

    @BeforeEach
    void setup() {
        vehicleRepository.deleteAllInBatch();
        ownerRepository.deleteAllInBatch();

        OwnerEntity existingOwner = ownerRepository.save(
                new OwnerEntity(
                        null,
                        "Maria Oliveira",
                        "111.444.777-35",
                        DocumentType.CPF,
                        "contact1@example.com"
                )
        );

        vehicleRepository.save(
                new VehicleEntity(
                        null,
                        "EXIST123",
                        "Audi A8",
                        VehicleStatus.ACTIVE,
                        existingOwner
                )
        );

        ownerId = ownerRepository.save(
                new OwnerEntity(
                        null,
                        "Paulo Silva",
                        "222.454.857-40",
                        DocumentType.CPF,
                        "contact2@example.com"
                )
        ).getId();
    }

    @Test
    @DisplayName("Given valid vehicle request When saving Then returns 201 Created with vehicle data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_vehicle:write"})
    void shouldCreateVehicleSuccessfully() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "BRA1S24",
                "Audi A8",
                ownerId
        );

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.plate").value(request.plate()))
                .andExpect(jsonPath("$.model").value(request.model()))
                .andExpect(jsonPath("$.ownerId").value(ownerId.toString()));
    }

    @Test
    @DisplayName("Given invalid vehicle request When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_vehicle:write"})
    void shouldReturnBadRequestWhenInvalidData() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "!!!",
                "",
                null
        );

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Given no authentication When saving vehicle Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuthPost() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "BRA1S24",
                "Audi A8",
                ownerId
        );

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When saving vehicle Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_vehicle:read"})
    void shouldReturnForbiddenWhenNoPermissionPost() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "BRA1S24",
                "Audi A8",
                ownerId
        );

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given existing vehicle When updating status Then returns 200 OK with updated vehicle")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_vehicle:write"})
    void shouldUpdateVehicleStatusSuccessfully() throws Exception {

        VehicleEntity vehicle = vehicleRepository.findAll().get(0);

        mockMvc.perform(patch("/vehicles/{id}", vehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicle.getId().toString()))
                .andExpect(jsonPath("$.plate").value(vehicle.getPlate()))
                .andExpect(jsonPath("$.model").value(vehicle.getModel()))
                .andExpect(jsonPath("$.ownerId").value(vehicle.getOwner().getId().toString()));
    }

    @Test
    @DisplayName("Given non existing vehicle When updating status Then returns 404 Not Found")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_vehicle:write"})
    void shouldReturnNotFoundWhenVehicleDoesNotExist() throws Exception {

        UUID nonExistingId = UUID.randomUUID();

        mockMvc.perform(patch("/vehicles/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Given no authentication When updating vehicle Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuthPatch() throws Exception {

        VehicleEntity vehicle = vehicleRepository.findAll().get(0);

        mockMvc.perform(patch("/vehicles/{id}", vehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When updating vehicle Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_vehicle:read"})
    void shouldReturnForbiddenWhenNoPermissionPatch() throws Exception {

        VehicleEntity vehicle = vehicleRepository.findAll().get(0);

        mockMvc.perform(patch("/vehicles/{id}", vehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}