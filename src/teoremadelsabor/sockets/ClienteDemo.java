package teoremadelsabor.sockets;

import java.io.*;
import java.net.*;

/**
 * Cliente simple para demostrar la conexion al servidor de notificaciones
 * Alternativa a telnet para Windows
 */
public class ClienteDemo {
  public static void main(String[] args) {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║  CLIENTE DE NOTIFICACIONES - DEMO     ║");
    System.out.println("╚════════════════════════════════════════╝\n");

    try (Socket socket = new Socket("localhost", 8080);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      System.out.println("✓ Conectado al servidor en localhost:8080");
      System.out.println("✓ Esperando notificaciones del servidor...\n");
      System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

      // Leer mensajes del servidor
      String mensaje;
      while ((mensaje = in.readLine()) != null) {
        System.out.println("📨 " + mensaje);
      }

    } catch (UnknownHostException e) {
      System.err.println("❌ No se pudo encontrar el host: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("❌ Error de conexión: " + e.getMessage());
      System.err.println("\n💡 Asegúrate de que el servidor esté corriendo primero:");
      System.err.println("   java -cp bin teoremadelsabor.main.Main");
    }
  }
}
