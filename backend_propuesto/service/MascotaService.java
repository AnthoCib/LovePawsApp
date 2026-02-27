package com.example.backendpropuesto.service;

import com.example.backendpropuesto.dto.MascotaRequestDto;
import com.example.backendpropuesto.entity.Mascota;
import com.example.backendpropuesto.repository.MascotaRepository;
import org.springframework.stereotype.Service;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public Mascota registrar(MascotaRequestDto request) {
        Mascota mascota = new Mascota();
        mascota.setNombre(request.nombre);
        mascota.setRaza(request.raza);
        mascota.setEdad(request.edad);
        mascota.setSexo(request.sexo);
        mascota.setDescripcion(request.descripcion);
        mascota.setFotoUrl(request.fotoUrl);
        mascota.setEstado(request.estado);
        return mascotaRepository.save(mascota);
    }
}
