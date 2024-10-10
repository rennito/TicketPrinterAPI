package org.devsystem.TicketPrinterAPI.service;

import org.devsystem.TicketPrinterAPI.database.DatabaseConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketService {

    // Método para obtener solo la hora
    private String obtenerHoraActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        return ahora.format(formatoHora);
    }

    // Método para generar el contenido del ticket
    public String generarContenidoTicket(String numeroPlaca, String horaEntrada, String horaSalida, Double montoTotal) {
        StringBuilder sb = new StringBuilder();
        sb.append("******** TICKET VEHÍCULO ********\n");
        sb.append(String.format("%-20s: %s\n", "Número de Placa", numeroPlaca));

        // Obtener solo la hora actual
        String horaActual = obtenerHoraActual();

        sb.append(String.format("%-20s: %s\n", "Hora de Entrada", horaEntrada));
        sb.append(String.format("%-20s: %s\n", "Hora de Salida", horaSalida));
        sb.append(String.format("%-20s: $%.2f\n", "Monto Total", montoTotal));
        sb.append(String.format("%-20s: %s\n", "Hora Actual", horaActual)); // Agregar hora actual
        sb.append("\n\n\n"); // Aumenta el número de saltos de línea
        sb.append("Gracias por su visita!\n"); // Sin "¡"
        sb.append("\n\n\n"); // Aumenta el número de saltos de línea
        return sb.toString();
    }

    // Método para obtener el ticket desde la base de datos
    public String obtenerTicketDesdeBaseDeDatos(int registroId) {
        String ticketContenido = "";
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT numero_placa, hora_entrada, hora_salida, monto_total FROM registro_vehiculos WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, registroId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String numeroPlaca = resultSet.getString("numero_placa");
                // Obtener hora en formato HH:mm
                String horaEntrada = formatearHora(resultSet.getTimestamp("hora_entrada"));
                String horaSalida = formatearHora(resultSet.getTimestamp("hora_salida"));
                Double montoTotal = resultSet.getDouble("monto_total");
                ticketContenido = generarContenidoTicket(numeroPlaca, horaEntrada, horaSalida, montoTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar excepciones adecuadamente
        }
        return ticketContenido;
    }

    // Método para formatear la hora a HH:mm
    private String formatearHora(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            return timestamp.toLocalDateTime().format(formatoHora);
        }
        return ""; // Retornar una cadena vacía si el timestamp es nulo
    }

    // Método para enviar el ticket a una terminal de impresión
    public boolean enviarATerminalDeImpresion(String contenido) {
        try (InputStream inputStream = new ByteArrayInputStream(contenido.getBytes())) {
            // Crea un objeto de documento
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            DocPrintJob printJob = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();

            // Crea un documento para imprimir
            Doc doc = new SimpleDoc(inputStream, flavor, null);

            // Configura las propiedades de impresión
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1)); // Cantidad de copias

            // Mensaje para iniciar la impresión
            System.out.println("Iniciando impresión...");

            // Imprime el documento
            printJob.print(doc, pras);

            // Mensaje de resultado de impresión
            System.out.println("Resultado de impresión: éxito");
            return true; // Impresión exitosa
        } catch (PrintException e) {
            e.printStackTrace();
            System.out.println("Resultado de impresión: error - " + e.getMessage());
            return false; // Error al imprimir
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Resultado de impresión: error de IO - " + e.getMessage());
            return false; // Error de IO
        }
    }
}
