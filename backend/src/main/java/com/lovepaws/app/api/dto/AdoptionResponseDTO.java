package com.lovepaws.app.api.dto;

public class AdoptionResponseDTO {
    private Long id;
    private String mensaje;
    private Long solicitudId;

    public AdoptionResponseDTO() {}

    public AdoptionResponseDTO(Long id, String mensaje, Long solicitudId) {
        this.id = id;
        this.mensaje = mensaje;
        this.solicitudId = solicitudId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Long getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }
}
