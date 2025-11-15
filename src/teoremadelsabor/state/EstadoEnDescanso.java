package teoremadelsabor.state;

import java.time.LocalTime;

/**
 * Clase que representa el estado en descanso (temporalmente)
 */
public class EstadoEnDescanso implements EstadoPuesto {

  /** 
   * {@inheritDoc} 
   */ 
  @Override
  public boolean disponibilidad(LocalTime hora) {
    // Simula pausa entre 15:00 y 16:00
    return !(hora.isAfter(LocalTime.of(15, 0)) && hora.isBefore(LocalTime.of(16, 0)));
  }
  
  /** 
   * {@inheritDoc} 
   */ 
  @Override
  public String getNombre() {
    return "En descanso";
  }
}

