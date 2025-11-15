package teoremadelsabor.observer;

import teoremadelsabor.mvc.PuestoComida;

/**
 * Interfaz que representa al observador.
 */

public interface Observador {
  
  /**
   * Metodo para cuando el sujeto (puesto) notifica un cambio
   * @param puesto Puesto que hizo la notificacion.
   * @param notificacion Cambio de estado (abierto, cerrado, en descanso).
   */
  void actualizar(PuestoComida puesto, String notificacion);
}

