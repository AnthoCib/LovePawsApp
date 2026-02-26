package com.lovepaws.app.service;

import com.lovepaws.app.api.dto.AdoptionRequestDTO;
import com.lovepaws.app.api.dto.AdoptionResponseDTO;
import com.lovepaws.app.domain.SolicitudAdopcion;

public interface AdoptionService {
    AdoptionResponseDTO createAdoptionRequest(AdoptionRequestDTO request);
    SolicitudAdopcion getById(Long id);
}
