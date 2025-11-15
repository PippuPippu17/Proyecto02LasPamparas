package teoremadelsabor.factory;

import java.util.Map;
import teoremadelsabor.mvc.PuestoComida;
import java.time.LocalTime;

/**
 * Clase Factory: crea instancias de PuestoComida.
 */
public class PuestoFactory {

  /**
   * Crea un nuevo puesto de comida a partir de los datos dados
   * @param datos Mapa con los datos del puesto
   * @return Instancia de PuestoComida 
   */
  public static PuestoComida crearPuesto(Map<String, String> datos) {
    return new PuestoComida(
      datos.get("id"),
      datos.get("nombre"),
      datos.get("tipoComida"),
      datos.get("ubicacion"),
      Double.parseDouble(datos.get("precioPromedio")),
      LocalTime.parse(datos.get("horaApertura")), 
      LocalTime.parse(datos.get("horaCierre")),
      datos.get("metodosPago") 
    );
  }
}

