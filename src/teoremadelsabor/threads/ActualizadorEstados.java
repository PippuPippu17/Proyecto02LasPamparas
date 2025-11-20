package teoremadelsabor.threads;

import teoremadelsabor.mvc.GestorPuestos;
import teoremadelsabor.sockets.ServidorNotificaciones;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread que actualiza automaticamente los estados de los puestos
 * segun la hora actual cada cierto intervalo de tiempo.
 * Integrado con el servidor de sockets para enviar notificaciones
 */
public class ActualizadorEstados extends Thread {

  private GestorPuestos gestor;
  private ServidorNotificaciones servidor;
  private volatile boolean ejecutando;
  private int intervaloSegundos;
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  /**
   * Crea un nuevo actualizador de estados
   * @param gestor Gestor de puestos
   * @param intervaloSegundos Intervalo en segundos entre actualizaciones
   */
  public ActualizadorEstados(GestorPuestos gestor, int intervaloSegundos) {
    this(gestor, null, intervaloSegundos);
  }

  /**
   * Crea un nuevo actualizador de estados con servidor de sockets
   * @param gestor Gestor de puestos
   * @param servidor Servidor de notificaciones (opcional)
   * @param intervaloSegundos Intervalo en segundos entre actualizaciones
   */
  public ActualizadorEstados(GestorPuestos gestor, ServidorNotificaciones servidor, int intervaloSegundos) {
    this.gestor = gestor;
    this.servidor = servidor;
    this.intervaloSegundos = intervaloSegundos;
    this.ejecutando = true;
    this.setDaemon(true); // Thread demonio para que no bloquee el cierre
    this.setName("ActualizadorEstados");
  }

  @Override
  public void run() {
    System.out.println("[THREAD] Actualizador de estados iniciado (cada " + intervaloSegundos + "s)");

    if (servidor != null) {
      System.out.println("[THREAD] Integrado con servidor de sockets en puerto 8080");
    }

    while (ejecutando) {
      try {
        Thread.sleep(intervaloSegundos * 1000L);

        LocalTime ahora = LocalTime.now();
        gestor.actualizarEstados(ahora);

        String horaFormateada = ahora.format(FORMATTER);
        int numPuestos = gestor.getPuestos().size();
        String mensaje = "Estados actualizados a las " + horaFormateada +
                        " (" + numPuestos + " puestos procesados)";

        System.out.println("\n[THREAD] " + mensaje);

        // Enviar notificacion via sockets a todos los clientes conectados
        if (servidor != null && servidor.getNumeroClientes() > 0) {
          servidor.notificarTodos(mensaje);
          System.out.println("[THREAD] Notificacion enviada a " + servidor.getNumeroClientes() + " cliente(s)");
        }

      } catch (InterruptedException e) {
        System.out.println("[THREAD] Actualizador interrumpido");
        break;
      } catch (Exception e) {
        System.err.println("[THREAD] Error durante actualizacion: " + e.getMessage());
      }
    }

    System.out.println("[THREAD] Actualizador de estados detenido");
  }

  /**
   * Detiene el thread de forma segura
   */
  public void detener() {
    ejecutando = false;
    this.interrupt();
  }

  /**
   * Verifica si el thread esta ejecutando
   * @return true si esta ejecutando, false si no
   */
  public boolean isEjecutando() {
    return ejecutando;
  }

  /**
   * Establece el servidor de notificaciones
   * @param servidor Servidor de notificaciones
   */
  public void setServidor(ServidorNotificaciones servidor) {
    this.servidor = servidor;
  }
}
