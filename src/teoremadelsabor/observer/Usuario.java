package teoremadelsabor.observer;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Observador que representa a un usuario
 */

public class Usuario implements Observador {
  private String nombre;

  /**
   * Crea un nuevo usuario (observador)
   * @param nombre Nombre del usuario.
   */
  public Usuario(String nombre) {
    this.nombre = nombre;
  }

  /** 
   * {@inheritDoc} 
   */
  @Override
  public void actualizar(PuestoComida puesto, String notificacion) {
    System.out.println("Notificación para " + nombre + ": El puesto " +
    puesto.getNombre() + " ha cambiado de estado a " + notificacion + ".");
  }
}

