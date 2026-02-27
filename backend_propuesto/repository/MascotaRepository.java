package com.example.backendpropuesto.repository;

import com.example.backendpropuesto.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
}
