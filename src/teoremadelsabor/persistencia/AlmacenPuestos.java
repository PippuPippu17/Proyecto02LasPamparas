package teoremadelsabor.persistencia;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import teoremadelsabor.mvc.PuestoComida;
import teoremadelsabor.factory.PuestoFactory;

/**
 * Maneja la lectura y escritura de puestos en archivo plano.
 */
public class AlmacenPuestos {
  
  private static final String RUTA = "data/puestos.txt";


  /**
   * Guarda los puestos en archivo TXT.
   * @param puestos Lista de puestos a guardar.
   */
  public static void guardarPuestos(List<PuestoComida> puestos) {
    File archivoOriginal = new File(RUTA);
    File archivoTemporal = new File(RUTA + ".tmp");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal))) {

      for (PuestoComida p : puestos) {
        writer.write(String.format("%s;%s;%s;%s;%.2f;%s;%s;%s%n",
          p.getId(), p.getNombre(), p.getTipo(), p.getUbicacion(),
          p.getPrecioPromedio(), p.getHoraApertura(),
          p.getHoraCierre(), p.getMetodosPago()
        ));
      }
      if (archivoOriginal.exists()) archivoOriginal.delete();
      archivoTemporal.renameTo(archivoOriginal);
    } catch (IOException e) {
        System.err.println("Error al guardar puestos: " + e.getMessage());
    }
  }
  /**
   * Carga los puestos desde archivo TXT.
   * Formato: id;nombre;ubicacion;tipoComida;horaApertura;horaCierre;metodosPago
   * @return puestos de comida almacenados
   */
  public static List<PuestoComida> cargarPuestos() {
    List<PuestoComida> lista = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
      String linea;

      while ((linea = br.readLine()) != null) {
        String[] partes = linea.split(";");

        if (partes.length != 8) {
          System.err.println("Línea invalida en puestos.txt: " + linea);
          continue;
        }

        String id = partes[0];
        String nombre = partes[1];
        String tipoComida = partes[2];
        String ubicacion = partes[3];
        double precio = Double.parseDouble(partes[4]);
        LocalTime apertura = LocalTime.parse(partes[5]);
        LocalTime cierre = LocalTime.parse(partes[6]);
        String metodosPago = partes[7];

        PuestoComida puesto = new PuestoFactory().crearPuesto(Map.of(
          "id", id, "nombre", nombre, "ubicacion", ubicacion, "tipoComida", tipoComida,
          "precioPromedio", precio + "", "horaApertura", apertura.toString(),
          "horaCierre", cierre.toString(), "metodosPago", metodosPago
        ));

        lista.add(puesto);
      }
    } catch (Exception e) {
      System.err.println("Error leyendo puestos: " + e.getMessage());
    }
    return lista;
  }
}

