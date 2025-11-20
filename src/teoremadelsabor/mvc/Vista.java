package teoremadelsabor.mvc;

import java.util.Scanner;
import teoremadelsabor.strategy.*;
import teoremadelsabor.persistencia.GeneradorReportes;
import teoremadelsabor.api.ServicioClima;
import java.io.IOException;

/**
 * Vista en consola para interactuar 
 */
public class Vista {

  private ControladorPuestos controlador;
  private Scanner uwu;

  /**
   * Crea la vista con el controlador 
   * @param controlador Controlador principal
   */
  public Vista(ControladorPuestos controlador) {
    this.controlador = controlador;
    this.uwu = new Scanner(System.in);
  }

  /**
   * Muestra el menú principal en consola.
   */
  public void menu() {

    while (true) {
      try {
        System.out.println("\n========== Teorema del Sabor ==========");
        System.out.println("1. Mostrar todos los puestos");
        System.out.println("2. Mostrar puestos por zonas");
        System.out.println("3. Mostrar puestos abiertos ahora");
        System.out.println("4. Recomendar por precio");
        System.out.println("5. Recomendar por tipo");
        System.out.println("6. Recomendar por ubicacion");
        System.out.println("7. Suscribirse a notificaciones de un puesto");
        System.out.println("8. Generar reporte (Archivo)");
        System.out.println("9. Ver clima actual (API)");
        System.out.println("10. Salir");
        System.out.println("=======================================");

        System.out.print("Opcion: ");
        int opcion = Integer.parseInt(uwu.nextLine());

        switch (opcion) {

          case 1:
            controlador.mostrarPuestos();
            break;

          case 2:
            controlador.mostrarPorZonas();
            break;

          case 3:
            controlador.mostrarAbiertosPorZona();
            break;

          case 4:
            recomendarPorPrecio();
            break;

          case 5:
            recomendarPorTipo();
            break;

          case 6:
            recomendarPorUbicacion();
            break;

          case 7:
            suscribirUsuario();
            break;

          case 8:
            generarReporte();
            break;

          case 9:
            verClima();
            break;

          case 10:
            System.out.println("Hasta luego!");
            return;

          default:
            System.out.println("Opcion invalida.");
        }

      } catch (Exception e) {
        System.out.println("Entrada invalida. Intenta otra vez.");
      }
    }
  }

  /**
   * Metodo privado para recomendar por precio con validacion
   */
  private void recomendarPorPrecio() {
    while (true) {
      try {
        System.out.print("Precio maximo: ");
        double precio = Double.parseDouble(uwu.nextLine());
        controlador.recomendar(new RecomendacionPrecio(precio));
        break;
      } catch (NumberFormatException e) {
        System.out.println("Entrada invalida. Intenta otra vez.");
      }
    }
  }

  /**
   * Metodo privado para recomendar por tipo de comida con validacion
   */
  private void recomendarPorTipo() {
    while (true) {
      try {
        System.out.println("\nTipos de comida:");
        System.out.println("1. Comida Completa");
        System.out.println("2. Comida Rapida");
        System.out.println("3. Tacos");
        System.out.println("4. Snacks");
        System.out.println("5. Postres");
        System.out.print("Elige una opcion: ");

        int opcion = Integer.parseInt(uwu.nextLine());
        String tipo;

        switch (opcion) {
          case 1: tipo = "Comida Completa"; break;
          case 2: tipo = "Comida Rapida"; break;
          case 3: tipo = "Tacos"; break;
          case 4: tipo = "Snacks"; break;
          case 5: tipo = "Postres"; break;
          default:
            System.out.println("Opcion invalida. Intenta otra vez.");
            continue;
        }

        controlador.recomendar(new RecomendacionTipo(tipo));
        break;
      } catch (NumberFormatException e) {
        System.out.println("Entrada invalida. Intenta otra vez.");
      }
    }
  }

  /**
   * Metodo privado para recomendar por ubicacion con validacion
   */
  private void recomendarPorUbicacion() {
    while (true) {
      try {
        System.out.println("\nUbicaciones:");
        System.out.println("1. Comedor");
        System.out.println("2. Estacionamiento");
        System.out.println("3. Media Luna");
        System.out.print("Elige una opcion: ");

        int opcion = Integer.parseInt(uwu.nextLine());
        String ubicacion;

        switch (opcion) {
          case 1: ubicacion = "Comedor"; break;
          case 2: ubicacion = "Estacionamiento"; break;
          case 3: ubicacion = "Media Luna"; break;
          default:
            System.out.println("Opcion invalida. Intenta otra vez.");
            continue;
        }

        controlador.recomendar(new RecomendacionUbicacion(ubicacion));
        break;
      } catch (NumberFormatException e) {
        System.out.println("Entrada invalida. Intenta otra vez.");
      }
    }
  }

  /**
   * Metodo privado para suscribir un usuario a un puesto
   */
  private void suscribirUsuario() {
    try {
      System.out.print("Nombre del usuario: ");
      String nombre = uwu.nextLine();

      // Mostrar todos los puestos con sus IDs
      System.out.println("\n--- Puestos disponibles ---");
      controlador.mostrarPuestos();

      System.out.print("\nID del puesto al que deseas suscribirte: ");
      String id = uwu.nextLine();

      if (controlador.suscribirUsuario(nombre, id)) {
        System.out.println("Suscripcion exitosa! Recibiras notificaciones del puesto.");
      } else {
        System.out.println("Puesto no encontrado.");
      }
    } catch (Exception e) {
      System.out.println("Error al suscribir usuario: " + e.getMessage());
    }
  }

  /**
   * Genera reporte de puestos en archivo
   * Demuestra escritura de archivos
   */
  private void generarReporte() {
    try {
      System.out.println("\nFormatos disponibles:");
      System.out.println("1. TXT (Texto plano)");
      System.out.println("2. CSV (Excel compatible)");
      System.out.print("Elige formato: ");

      int formato = Integer.parseInt(uwu.nextLine());

      if (formato == 1) {
        GeneradorReportes.generarReporteTXT(
          controlador.getGestor().getPuestos(),
          "reporte_puestos.txt"
        );
        System.out.println("✓ Reporte generado: reporte_puestos.txt");
      } else if (formato == 2) {
        GeneradorReportes.generarReporteCSV(
          controlador.getGestor().getPuestos(),
          "reporte_puestos.csv"
        );
        System.out.println("✓ Reporte generado: reporte_puestos.csv");
      } else {
        System.out.println("Formato invalido.");
      }

      // Escribir en log
      GeneradorReportes.escribirLog("Reporte generado por usuario", "actividad.log");

    } catch (IOException e) {
      System.out.println("Error generando reporte: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Entrada invalida.");
    }
  }

  /**
   * Consulta clima usando API externa
   * Demuestra integracion con API REST
   */
  private void verClima() {
    System.out.println("\n--- Consultando API de Clima ---");
    ServicioClima.ClimaInfo climaInfo = ServicioClima.obtenerClimaInfo();

    System.out.println(climaInfo);
  }
}

