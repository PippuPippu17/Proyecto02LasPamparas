package teoremadelsabor.composite;

import java.time.LocalTime;
import java.util.List;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Componente base de composite
 * Representa puesto individual o zona de varios puestos
 */
public interface ComponenteZona {

  /**
   * Muestra la información del componente (puesto o zona con puestos).
   */
  void mostrar();

  /**
   * Lista todos los puestos que estan abiertos en este componente
   * @param hora Hora actual para verificar disponibilidad
   * @return Lista de puestos abiertos
   */
  List<PuestoComida> listarAbiertos(LocalTime hora);

  /**
   * Obtiene el numero total de puestos en este componente
   * @return Cantidad de puestos
   */
  int contarPuestos();
}

