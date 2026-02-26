package com.lovepaws.app.persistence;

import com.lovepaws.app.domain.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRequestRepository extends JpaRepository<SolicitudAdopcion, Long> {
}
