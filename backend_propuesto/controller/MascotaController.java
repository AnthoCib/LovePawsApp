package com.example.backendpropuesto.controller;

import com.example.backendpropuesto.dto.MascotaRequestDto;
import com.example.backendpropuesto.entity.Mascota;
import com.example.backendpropuesto.service.MascotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@RequestBody MascotaRequestDto request) {
        return ResponseEntity.ok(mascotaService.registrar(request));
    }
}
