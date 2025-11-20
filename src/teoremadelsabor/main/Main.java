package teoremadelsabor.main;

import teoremadelsabor.mvc.*;
import teoremadelsabor.gui.VentanaPrincipal;
import teoremadelsabor.threads.ActualizadorEstados;
import teoremadelsabor.sockets.ServidorNotificaciones;
import teoremadelsabor.api.ServicioClima;
import javax.swing.SwingUtilities;
import java.util.Scanner;

/**
 * Clase principal del programa.
 * Integra todas las funcionalidades: MVC, Threads, Sockets, API, GUI
 */
public class Main {
  public static void main(String[] args) {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║    TEOREMA DEL SABOR - FACULTAD       ║");
    System.out.println("║         Sistema de Puestos            ║");
    System.out.println("╚════════════════════════════════════════╝");

    // Inicializar componentes MVC
    GestorPuestos gestor = new GestorPuestos();
    ControladorPuestos controlador = new ControladorPuestos(gestor);

    // PUNTO EXTRA: Sockets - Servidor de notificaciones
    ServidorNotificaciones servidor = new ServidorNotificaciones(8080);
    servidor.start();

    // PUNTO EXTRA: Thread - Actualizador automático de estados (integrado con sockets)
    ActualizadorEstados actualizador = new ActualizadorEstados(gestor, servidor, 60);
    actualizador.start();

    // PUNTO EXTRA: API - Mostrar clima al inicio
    ServicioClima.ClimaInfo climaInfo = ServicioClima.obtenerClimaInfo();
    System.out.println("\n" + climaInfo);

    // Agregar hook para cerrar threads al salir
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("\n[SISTEMA] Cerrando threads...");
      actualizador.detener();
      servidor.detener();
    }));

    // PUNTO EXTRA: GUI - Elegir entre interfaz gráfica o consola
    System.out.println("\n¿Qué interfaz deseas usar?");
    System.out.println("1. Interfaz Gráfica (GUI)");
    System.out.println("2. Interfaz de Consola");
    System.out.print("Opción: ");

    try (Scanner scanner = new Scanner(System.in)) {
      String opcion = scanner.nextLine().trim();

      if (opcion.equals("1")) {
        // Iniciar GUI en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
          VentanaPrincipal ventana = new VentanaPrincipal(controlador);
          ventana.setVisible(true);
        });
      } else {
        // Iniciar interfaz de consola
        Vista vista = new Vista(controlador);
        vista.menu();
      }
    }
  }
}

