package teoremadelsabor.state;

import java.time.LocalTime;

/**
 * Clase que representa el estado  cerrado de un puesto.
 */
public class EstadoCerrado implements EstadoPuesto {

  /** 
   * {@inheritDoc} 
   */
  @Override
  public boolean disponibilidad(LocalTime hora) {
    return false;
  }

  /** 
   * {@inheritDoc} 
   */
  @Override
  public String getNombre() {
    return "Cerrado";
  }
}

