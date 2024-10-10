package org.devsystem.TicketPrinterAPI.model;

public class TicketRequest {
    private Integer registroId; // Campo para almacenar el ID del registro
    private String numeroPlaca;
    private String horaEntrada;
    private String horaSalida;
    private Double montoTotal;

    // Constructor por defecto
    public TicketRequest() {}

    // Constructor con parámetros
    public TicketRequest(Integer registroId, String numeroPlaca, String horaEntrada, String horaSalida, Double montoTotal) {
        setRegistroId(registroId); // Usar el setter para la validación
        setNumeroPlaca(numeroPlaca); // Usar el setter para la validación
        setHoraEntrada(horaEntrada); // Usar el setter para la validación
        setHoraSalida(horaSalida); // Usar el setter para la validación
        setMontoTotal(montoTotal); // Usar el setter para la validación
    }

    // Getters y Setters
    public Integer getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Integer registroId) {
        if (registroId == null || registroId <= 0) {
            throw new IllegalArgumentException("El ID del registro debe ser un número positivo.");
        }
        this.registroId = registroId;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        if (numeroPlaca == null || numeroPlaca.isEmpty()) {
            throw new IllegalArgumentException("El número de placa no puede estar vacío.");
        }
        this.numeroPlaca = numeroPlaca;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        if (horaEntrada == null || horaEntrada.isEmpty()) {
            throw new IllegalArgumentException("La hora de entrada no puede estar vacía.");
        }
        // Aquí podrías agregar una validación de formato de hora si es necesario
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        if (horaSalida == null || horaSalida.isEmpty()) {
            throw new IllegalArgumentException("La hora de salida no puede estar vacía.");
        }
        // Aquí podrías agregar una validación de formato de hora si es necesario
        this.horaSalida = horaSalida;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        if (montoTotal == null || montoTotal < 0) {
            throw new IllegalArgumentException("El monto total no puede ser nulo o negativo.");
        }
        this.montoTotal = montoTotal;
    }

    // Método toString
    @Override
    public String toString() {
        return "TicketRequest{" +
                "registroId=" + registroId + 
                ", numeroPlaca='" + numeroPlaca + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSalida='" + horaSalida + '\'' +
                ", montoTotal=" + montoTotal +
                '}';
    }
}
