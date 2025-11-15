package teoremadelsabor.mvc;

import java.util.Scanner;
import teoremadelsabor.strategy.*;
import teoremadelsabor.observer.Usuario;

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
        System.out.println("\n== Teorema del Sabor ==");
        System.out.println("1. Mostrar puestos");
        System.out.println("2. Recomendar por precio");
        System.out.println("3. Recomendar por tipo");
        System.out.println("4. Recomendar por ubicación");
        System.out.println("5. Suscribirse a un puesto");
        System.out.println("6. Actualizar estados (por hora actual)");
        System.out.println("7. Salir");

        System.out.print("Opcion: ");
        int opcion = Integer.parseInt(uwu.nextLine());

        switch (opcion) {

          case 1:
          controlador.mostrarPuestos();
          break;

          case 2:
          System.out.print("Precio maximo: ");
          double precio = Double.parseDouble(uwu.nextLine());
          controlador.recomendar(new RecomendacionPrecio(precio));
          break;

          case 3:
          System.out.println("Comida Completa, Comida Rapida, Tacos, Snacks, Postres");
          System.out.print("Elige un tipo de comida: ");
          String tipo = uwu.nextLine();
          controlador.recomendar(new RecomendacionTipo(tipo));
          break;
          
          case 4:
          System.out.println("Comedor, Estacionamiento, Media Luna");
          System.out.print("Ubicacion: ");
          String ubic = uwu.nextLine();
          controlador.recomendar(new RecomendacionUbicacion(ubic));
          break;
          
          case 5:
          suscribirUsuario();       
          break;

          case 6:
          controlador.actualizarSegunHora();
          System.out.println("Estados actualizados.");
          break;

          case 7:
          System.out.println("¡Hasta luego!");
          return;

          default:
          System.out.println("Opcion invalida.");
        }

      } catch (Exception e) {
        System.out.println("Entrada invalida. Intenta otra vez.");
      }
    }
  }

  private void suscribirUsuario() {
    try {
      System.out.print("Nombre del usuario: ");
      String nombre = uwu.nextLine();
      Usuario usr = new Usuario(nombre);

      System.out.print("ID del puesto al que deseas suscribirte: ");
      String id = uwu.nextLine();

      for (PuestoComida p : controlador.getGestor().getPuestos()) {
        if (p.getId().equals(id)) {
          p.suscribir(usr);
          System.out.println(nombre + " se ha suscrito a " + p.getNombre());
          return;
        }
      }

      System.out.println("Puesto no encontrado.");
    } catch (Exception e) {
      System.out.println("Error al suscribir usuario.");
    }
  }
}

