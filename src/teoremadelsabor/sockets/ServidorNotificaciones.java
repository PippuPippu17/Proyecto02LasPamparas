package teoremadelsabor.sockets;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Servidor que envia notificaciones a clientes conectados
 * Demuestra uso de Sockets para comunicacion en red
 */
public class ServidorNotificaciones extends Thread {

  private int puerto;
  private volatile boolean ejecutando;
  private List<PrintWriter> clientes;
  private ServerSocket serverSocket;

  /**
   * Crea un nuevo servidor de notificaciones
   * @param puerto Puerto en el que escuchara
   */
  public ServidorNotificaciones(int puerto) {
    this.puerto = puerto;
    this.ejecutando = true;
    this.clientes = Collections.synchronizedList(new ArrayList<>());
    this.setDaemon(true);
    this.setName("ServidorNotificaciones");
  }

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(puerto);
      System.out.println("[SERVIDOR] Iniciado en puerto " + puerto);

      while (ejecutando) {
        try {
          Socket clientSocket = serverSocket.accept();
          System.out.println("[SERVIDOR] Cliente conectado: " + clientSocket.getInetAddress());

          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
          clientes.add(out);

          // Thread para manejar cliente
          new Thread(() -> manejarCliente(clientSocket, out)).start();

        } catch (IOException e) {
          if (ejecutando) {
            System.err.println("[SERVIDOR] Error aceptando cliente: " + e.getMessage());
          }
        }
      }

    } catch (IOException e) {
      System.err.println("[SERVIDOR] Error iniciando servidor: " + e.getMessage());
    }
  }

  /**
   * Maneja la comunicacion con un cliente
   * @param socket Socket del cliente
   * @param out Stream de salida
   */
  private void manejarCliente(Socket socket, PrintWriter out) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      out.println("Conectado al servidor de notificaciones de Teorema del Sabor");

      String mensaje;
      while ((mensaje = in.readLine()) != null) {
        System.out.println("[SERVIDOR] Recibido: " + mensaje);

        if (mensaje.equalsIgnoreCase("SALIR")) {
          break;
        }
      }

    } catch (IOException e) {
      System.err.println("[SERVIDOR] Error con cliente: " + e.getMessage());
    } finally {
      clientes.remove(out);
      try {
        socket.close();
      } catch (IOException e) {
        // Ignorar
      }
    }
  }

  /**
   * Envia una notificacion a todos los clientes conectados
   * @param mensaje Mensaje a enviar
   */
  public void notificarTodos(String mensaje) {
    synchronized (clientes) {
      for (PrintWriter cliente : clientes) {
        cliente.println("[NOTIFICACION] " + mensaje);
      }
    }
    System.out.println("[SERVIDOR] Notificacion enviada a " + clientes.size() + " cliente(s)");
  }

  /**
   * Detiene el servidor
   */
  public void detener() {
    ejecutando = false;
    try {
      if (serverSocket != null && !serverSocket.isClosed()) {
        serverSocket.close();
      }
    } catch (IOException e) {
      System.err.println("[SERVIDOR] Error cerrando servidor: " + e.getMessage());
    }
  }

  /**
   * Obtiene el numero de clientes conectados
   * @return Numero de clientes
   */
  public int getNumeroClientes() {
    return clientes.size();
  }
}
