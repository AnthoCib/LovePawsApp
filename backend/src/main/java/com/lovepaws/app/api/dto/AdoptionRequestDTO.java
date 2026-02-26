package com.lovepaws.app.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdoptionRequestDTO {
    @NotNull
    private Long mascotaId;

    @NotBlank
    private String solicitanteNombre;

    @Email
    @NotBlank
    private String solicitanteEmail;

    @NotBlank
    private String solicitanteTelefono;

    @NotBlank
    private String direccion;

    @NotBlank
    private String motivo;

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
}
