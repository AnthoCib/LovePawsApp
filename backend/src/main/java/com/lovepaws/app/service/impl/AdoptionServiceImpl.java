package com.lovepaws.app.service.impl;

import com.lovepaws.app.api.dto.AdoptionRequestDTO;
import com.lovepaws.app.api.dto.AdoptionResponseDTO;
import com.lovepaws.app.domain.SolicitudAdopcion;
import com.lovepaws.app.persistence.AdoptionRequestRepository;
import com.lovepaws.app.persistence.MascotaRepository;
import com.lovepaws.app.service.AdoptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final MascotaRepository mascotaRepository;

    public AdoptionServiceImpl(AdoptionRequestRepository adoptionRequestRepository, MascotaRepository mascotaRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    @Transactional
    public AdoptionResponseDTO createAdoptionRequest(AdoptionRequestDTO request) {
        if (!mascotaRepository.existsById(request.getMascotaId())) {
            throw new ResponseStatusException(NOT_FOUND, "No existe la mascota solicitada");
        }

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setMascotaId(request.getMascotaId());
        solicitud.setSolicitanteNombre(request.getSolicitanteNombre());
        solicitud.setSolicitanteEmail(request.getSolicitanteEmail());
        solicitud.setSolicitanteTelefono(request.getSolicitanteTelefono());
        solicitud.setDireccion(request.getDireccion());
        solicitud.setMotivo(request.getMotivo());

        SolicitudAdopcion saved = adoptionRequestRepository.save(solicitud);
        return new AdoptionResponseDTO(saved.getId(), "Solicitud de adopción creada con éxito", saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudAdopcion getById(Long id) {
        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Solicitud no encontrada"));
    }
}
