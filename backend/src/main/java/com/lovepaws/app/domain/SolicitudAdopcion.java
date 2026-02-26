package com.lovepaws.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "solicitud_adopcion")
public class SolicitudAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mascota_id", nullable = false)
    private Long mascotaId;

    @Column(name = "solicitante_nombre", nullable = false)
    private String solicitanteNombre;

    @Column(name = "solicitante_email", nullable = false)
    private String solicitanteEmail;

    @Column(name = "solicitante_telefono", nullable = false)
    private String solicitanteTelefono;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "fecha_solicitud")
    private OffsetDateTime fechaSolicitud;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }
    public String getSolicitanteNombre() { return solicitanteNombre; }
    public void setSolicitanteNombre(String solicitanteNombre) { this.solicitanteNombre = solicitanteNombre; }
    public String getSolicitanteEmail() { return solicitanteEmail; }
    public void setSolicitanteEmail(String solicitanteEmail) { this.solicitanteEmail = solicitanteEmail; }
    public String getSolicitanteTelefono() { return solicitanteTelefono; }
    public void setSolicitanteTelefono(String solicitanteTelefono) { this.solicitanteTelefono = solicitanteTelefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public OffsetDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(OffsetDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}
