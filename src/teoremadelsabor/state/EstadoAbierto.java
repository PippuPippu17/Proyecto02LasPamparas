package teoremadelsabor.state;

import java.time.LocalTime;

/**
 * Clase que  representa estado abierto del puesto.
 */
public class EstadoAbierto implements EstadoPuesto {

  /** 
   * {@inheritDoc} 
   */ 
  @Override
  public boolean disponibilidad(LocalTime hora) {
    return true;  
  }


  /** 
   * {@inheritDoc} 
   */
  @Override
  public String getNombre() {
    return "Abierto";
  }
}

