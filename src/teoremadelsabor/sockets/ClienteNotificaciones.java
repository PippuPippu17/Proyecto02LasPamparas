package teoremadelsabor.sockets;

import java.io.*;
import java.net.*;

/**
 * Cliente que se conecta al servidor de notificaciones
 * Demuestra comunicacion cliente-servidor con Sockets
 */
public class ClienteNotificaciones extends Thread {

  private String host;
  private int puerto;
  private volatile boolean ejecutando;
  private Socket socket;

  /**
   * Crea un nuevo cliente de notificaciones
   * @param host Host del servidor
   * @param puerto Puerto del servidor
   */
  public ClienteNotificaciones(String host, int puerto) {
    this.host = host;
    this.puerto = puerto;
    this.ejecutando = true;
    this.setDaemon(true);
    this.setName("ClienteNotificaciones");
  }

  @Override
  public void run() {
    try {
      socket = new Socket(host, puerto);
      System.out.println("[CLIENTE] Conectado al servidor " + host + ":" + puerto);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Leer mensajes del servidor
      String mensaje;
      while (ejecutando && (mensaje = in.readLine()) != null) {
        System.out.println("\n" + mensaje);
      }

    } catch (IOException e) {
      if (ejecutando) {
        System.err.println("[CLIENTE] Error: " + e.getMessage());
      }
    }
  }

  /**
   * Envia un mensaje al servidor
   * @param mensaje Mensaje a enviar
   */
  public void enviarMensaje(String mensaje) {
    try {
      if (socket != null && !socket.isClosed()) {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensaje);
      }
    } catch (IOException e) {
      System.err.println("[CLIENTE] Error enviando mensaje: " + e.getMessage());
    }
  }

  /**
   * Desconecta del servidor
   */
  public void desconectar() {
    ejecutando = false;
    try {
      if (socket != null && !socket.isClosed()) {
        enviarMensaje("SALIR");
        socket.close();
      }
    } catch (IOException e) {
      System.err.println("[CLIENTE] Error desconectando: " + e.getMessage());
    }
  }
}
