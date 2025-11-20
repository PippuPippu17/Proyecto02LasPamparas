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
  private static final Object lock = new Object();


  /**
   * Guarda los puestos en archivo TXT.
   * Escribe directamente en el archivo sin usar temporales.
   * @param puestos Lista de puestos a guardar.
   */
  public static void guardarPuestos(List<PuestoComida> puestos) {
    synchronized (lock) {
      // Crear directorio si no existe
      File dir = new File("data");
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // Escribir directamente en el archivo (sin archivo temporal)
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA))) {
        for (PuestoComida p : puestos) {
          writer.write(String.format("%s;%s;%s;%s;%.2f;%s;%s;%s%n",
            p.getId(), p.getNombre(), p.getTipo(), p.getUbicacion(),
            p.getPrecioPromedio(), p.getHoraApertura(),
            p.getHoraCierre(), p.getMetodosPago()
          ));
        }
        writer.flush(); // Asegurar que se escriba todo
        System.out.println("[PERSISTENCIA] " + puestos.size() + " puestos guardados en " + RUTA);
      } catch (IOException e) {
        System.err.println("Error al guardar puestos: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Carga los puestos desde archivo TXT.
   * Si el archivo no existe o está vacío, crea uno con datos por defecto.
   * Formato: id;nombre;tipoComida;ubicacion;precio;horaApertura;horaCierre;metodosPago
   * @return puestos de comida almacenados
   */
  public static List<PuestoComida> cargarPuestos() {
    synchronized (lock) {
      List<PuestoComida> lista = new ArrayList<>();
      File archivo = new File(RUTA);

      // Si el archivo no existe o está vacío, crear con datos por defecto
      if (!archivo.exists() || archivo.length() == 0) {
        System.out.println("[PERSISTENCIA] Archivo no existe o está vacío. Creando datos por defecto...");
        crearArchivoPorDefecto();
      }

      try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
        String linea;
        int lineNumber = 0;

        while ((linea = br.readLine()) != null) {
          lineNumber++;
          linea = linea.trim();

          if (linea.isEmpty()) continue; // Ignorar líneas vacías

          String[] partes = linea.split(";");

          if (partes.length != 8) {
            System.err.println("Línea " + lineNumber + " inválida (se esperan 8 campos): " + linea);
            continue;
          }

          try {
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
          } catch (Exception e) {
            System.err.println("Error parseando línea " + lineNumber + ": " + e.getMessage());
          }
        }

        System.out.println("[PERSISTENCIA] " + lista.size() + " puestos cargados desde " + RUTA);
      } catch (Exception e) {
        System.err.println("Error leyendo puestos: " + e.getMessage());
      }
      return lista;
    }
  }

  /**
   * Crea el archivo de puestos con datos por defecto si no existe.
   */
  private static void crearArchivoPorDefecto() {
    File dir = new File("data");
    if (!dir.exists()) {
      dir.mkdirs();
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
      // Formato: id;nombre;tipoComida;ubicacion;precio;horaApertura;horaCierre;metodosPago
      pw.println("1;Chilaquiles;Comida Rapida;Media Luna;50.00;08:00;18:00;Efectivo|Transferencia");
      pw.println("2;Capricho;Comida Rapida;Media Luna;50.00;08:00;19:00;Efectivo|Transferencia");
      pw.println("3;Snack Station;Comida Rapida;Media Luna;50.00;08:00;18:00;Efectivo|Transferencia");
      pw.println("4;Lulú;Comida Rapida;Media Luna;50.00;06:30;17:30;Efectivo|Transferencia");
      pw.println("5;Chapatas Y Enchiladas;Comida Rapida;Media Luna;50.00;08:00;18:30;Efectivo");
      pw.println("6;Los Dones Del Sabor;Comida Rapida;Media Luna;45.00;12:00;18:00;Efectivo|Transferencia");
      pw.println("7;Dulces Y Refrescos;Golosinas;Media Luna;45.00;09:30;17:30;Efectivo");
      pw.println("8;Tacos De Guisado;Tacos;Media Luna;30.00;11:30;17:30;Efectivo|Transferencia");
      pw.println("9;Gemela Derecha;Comida Rapida;Estacionamiento;60.00;07:00;20:00;Efectivo|Transferencia");
      pw.println("10;Gemela Izquierda;Comida Rapida;Estacionamiento;70.00;09:30;19:00;Efectivo|Transferencia");
      pw.println("11;La Michoacana;Postres;Puestos;30.00;10:00;17:00;Efectivo|Transferencia");
      pw.println("12;Nikkei;Asiatica;Comedor;60.00;11:30;18:00;Efectivo|Transferencia");
      pw.println("13;Harry's;Comida Rapida;Comedor;50.00;08:00;19:00;Efectivo|Transferencia");
      pw.println("14;Tía Aly;Comida Completa;Comedor;50.00;09:30;18:00;Efectivo|Transferencia");
      pw.println("15;Chilaquiles Express;Comida Rapida;Comedor;60.00;08:30;19:30;Efectivo");
      pw.println("16;Brujita De Limón;Postres;Puestos;30.00;07:00;20:00;Efectivo|Transferencia");
      pw.println("17;Pan Y Postres;Postres;Puestos;30.00;09:30;19:00;Efectivo|Transferencia");
      pw.println("18;Tienda O;Snacks;Edificio O;25.00;06:30;21:00;Efectivo");
      pw.println("19;Fresurita;Postres;Puestos;50.00;08:00;15:30;Efectivo");
      pw.println("20;Tienda Fondo;Snacks y Dulces;Comedor;20.00;08:00;19:00;Efectivo");
      pw.println("21;Tienda Inicio;Snacks;Comedor;30.00;07:00;21:30;Efectivo");
      pw.println("22;Cafetería;Comida Completa;Comedor;38.00;07:00;20:00;Efectivo");
      pw.println("23;Quesadillas Doña Mary;Quesadillas;Media Luna;38.00;08:00;17:00;Efectivo");
      pw.println("24;Pozole La Tradición;Pozole;Estacionamiento;65.00;10:00;20:00;Efectivo|Tarjeta");
      pw.println("25;Tortas Ahogadas;Tortas;Estacionamiento;50.00;09:00;19:00;Efectivo|Tarjeta");

      pw.flush();
      System.out.println("[PERSISTENCIA] Archivo creado con 25 puestos por defecto");
    } catch (IOException e) {
      System.err.println("Error creando archivo por defecto: " + e.getMessage());
    }
  }
}
