package teoremadelsabor.strategy;

import java.util.List;
import java.util.stream.Collectors;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Estrategia de recomendación basada en ubicacion
 */
public class RecomendacionUbicacion implements StrategyRecomendacion {
  private String ubicacion;

  /**
   * Crea una recomendación basada en la ubicación.
   * @param ubicacion Zona o ubicación deseada.
   */
  public RecomendacionUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  /** 
   * {@inheritDoc} 
   */  
  @Override
  public List<PuestoComida> recomendar(List<PuestoComida> puestos) {
    return puestos.stream()
      .filter(p -> p.getUbicacion().equalsIgnoreCase(ubicacion))
      .collect(Collectors.toList());
  }
}

