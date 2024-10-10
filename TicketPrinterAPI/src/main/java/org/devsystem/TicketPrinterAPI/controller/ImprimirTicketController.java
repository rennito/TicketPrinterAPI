package org.devsystem.TicketPrinterAPI.controller;

import org.devsystem.TicketPrinterAPI.model.TicketRequest;
import org.devsystem.TicketPrinterAPI.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImprimirTicketController {

    private final TicketService ticketService;

    // Inyección de dependencias para el servicio
    public ImprimirTicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/imprimirTicket")
    public ResponseEntity<Map<String, Object>> imprimirTicket(@RequestBody TicketRequest ticketRequest) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            // Validar si el registro_id está presente
            if (ticketRequest.getRegistroId() == null) {
                respuesta.put("success", false);
                respuesta.put("message", "ID del registro no proporcionado.");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }

            // Obtener el contenido del ticket desde la base de datos
            String ticketContenido = ticketService.obtenerTicketDesdeBaseDeDatos(ticketRequest.getRegistroId());

            // Validar si se encontró el ticket
            if (ticketContenido == null || ticketContenido.isEmpty()) {
                respuesta.put("success", false);
                respuesta.put("message", "No se encontró el ticket en la base de datos.");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }

            // Simular la impresión del ticket usando el servicio
            boolean resultadoImpresion = ticketService.enviarATerminalDeImpresion(ticketContenido);

            // Crear la respuesta para el cliente
            if (resultadoImpresion) {
                respuesta.put("success", true);
                respuesta.put("message", "Ticket impreso correctamente.");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.put("success", false);
                respuesta.put("message", "Error al imprimir el ticket.");
                return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            respuesta.put("success", false);
            respuesta.put("message", "Se produjo un error en el servidor: " + e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
