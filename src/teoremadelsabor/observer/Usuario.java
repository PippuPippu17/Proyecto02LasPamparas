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
   * Obtiene el nombre del usuario
   * @return Nombre del usuario
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actualizar(PuestoComida puesto, String notificacion) {
    System.out.println("\n[NOTIFICACION] Para: " + nombre);
    System.out.println("  Puesto: " + puesto.getNombre());
    System.out.println("  Evento: " + notificacion);
  }

  @Override
  public String toString() {
    return nombre;
  }
}

