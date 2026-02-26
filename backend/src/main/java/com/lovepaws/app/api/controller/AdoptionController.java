package com.lovepaws.app.api.controller;

import com.lovepaws.app.api.dto.AdoptionRequestDTO;
import com.lovepaws.app.api.dto.AdoptionResponseDTO;
import com.lovepaws.app.domain.SolicitudAdopcion;
import com.lovepaws.app.service.AdoptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adopciones")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    /**
     * Ejemplo request POST /api/adopciones
     * {
     *   "mascotaId": 6000,
     *   "solicitanteNombre": "Juan Perez",
     *   "solicitanteEmail": "juan@example.com",
     *   "solicitanteTelefono": "999888777",
     *   "direccion": "Av. Siempre Viva 123",
     *   "motivo": "Deseo adoptar un perro para mi familia"
     * }
     *
     * Ejemplo response 201
     * {
     *   "id": 8001,
     *   "mensaje": "Solicitud de adopción creada con éxito",
     *   "solicitudId": 8001
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionResponseDTO create(@Valid @RequestBody AdoptionRequestDTO request) {
        return adoptionService.createAdoptionRequest(request);
    }

    @GetMapping("/{id}")
    public SolicitudAdopcion getById(@PathVariable Long id) {
        return adoptionService.getById(id);
    }
}
