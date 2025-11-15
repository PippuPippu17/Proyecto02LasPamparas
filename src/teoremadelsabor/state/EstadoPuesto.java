package teoremadelsabor.state;

import java.time.LocalTime;

/**
 * Interfaz State.
 * Define el comportamiento que depende del estado del puesto.
 */
public interface EstadoPuesto {
  
  /**
   * Determina si el puesto esta abierto, segun la hora.
   * @param hora Hora actual.
   * @return true si el puesto esta abierto, false si esta cerrado
   */
  boolean disponibilidad(LocalTime hora);


  /**
   * Obtiene el  estado.
   * @return Nombre del estado.
   */
  String getNombre();
}

