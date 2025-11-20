package teoremadelsabor.persistencia;

import teoremadelsabor.mvc.PuestoComida;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Genera reportes en diferentes formatos (TXT, CSV)
 * Demuestra lectura y escritura avanzada de archivos
 */
public class GeneradorReportes {

  /**
   * Genera un reporte en formato TXT
   * @param puestos Lista de puestos
   * @param nombreArchivo Nombre del archivo de salida
   * @throws IOException Si hay error al escribir
   */
  public static void generarReporteTXT(List<PuestoComida> puestos, String nombreArchivo) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
      writer.write("========================================\n");
      writer.write("   REPORTE DE PUESTOS DE COMIDA\n");
      writer.write("========================================\n");
      writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
      writer.write("Total de puestos: " + puestos.size() + "\n");
      writer.write("========================================\n\n");

      for (PuestoComida p : puestos) {
        writer.write("ID: " + p.getId() + "\n");
        writer.write("Nombre: " + p.getNombre() + "\n");
        writer.write("Tipo: " + p.getTipo() + "\n");
        writer.write("Ubicacion: " + p.getUbicacion() + "\n");
        writer.write("Precio promedio: $" + p.getPrecioPromedio() + "\n");
        writer.write("Estado: " + p.getEstado().getNombre() + "\n");
        writer.write("Horario: " + p.getHoraApertura() + " - " + p.getHoraCierre() + "\n");
        writer.write("----------------------------------------\n");
      }

      writer.write("\nReporte generado exitosamente.\n");
    }
  }

  /**
   * Genera un reporte en formato CSV
   * @param puestos Lista de puestos
   * @param nombreArchivo Nombre del archivo de salida
   * @throws IOException Si hay error al escribir
   */
  public static void generarReporteCSV(List<PuestoComida> puestos, String nombreArchivo) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
      // Encabezados
      writer.println("ID,Nombre,Tipo,Ubicacion,PrecioPromedio,Estado,HoraApertura,HoraCierre");

      // Datos
      for (PuestoComida p : puestos) {
        writer.printf("%s,%s,%s,%s,%.2f,%s,%s,%s%n",
          p.getId(),
          p.getNombre(),
          p.getTipo(),
          p.getUbicacion(),
          p.getPrecioPromedio(),
          p.getEstado().getNombre(),
          p.getHoraApertura(),
          p.getHoraCierre()
        );
      }
    }
  }

  /**
   * Lee estadisticas de un archivo de log
   * @param nombreArchivo Archivo a leer
   * @return Contenido del archivo
   * @throws IOException Si hay error al leer
   */
  public static String leerLog(String nombreArchivo) throws IOException {
    StringBuilder contenido = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
      String linea;
      while ((linea = reader.readLine()) != null) {
        contenido.append(linea).append("\n");
      }
    }

    return contenido.toString();
  }

  /**
   * Escribe un log de actividad
   * @param mensaje Mensaje a registrar
   * @param nombreArchivo Archivo de log
   * @throws IOException Si hay error al escribir
   */
  public static void escribirLog(String mensaje, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo, true)) {
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      writer.write("[" + timestamp + "] " + mensaje + "\n");
    }
  }
}
