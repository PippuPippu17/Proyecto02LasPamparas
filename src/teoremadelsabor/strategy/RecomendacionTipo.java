package teoremadelsabor.strategy;

import java.util.List;
import java.util.stream.Collectors;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Estrategia de recomendacion basada en tipo de puesto.
 */
public class RecomendacionTipo implements StrategyRecomendacion {
  private String tipo;

  /**
   * Crea una recomendación por tipo de comida.
   * @param tipo Tipo de comida deseado.
   */
  public RecomendacionTipo(String tipo) {
    this.tipo = tipo;
  }
  
  /** 
   * {@inheritDoc} 
   */  
  @Override
  public List<PuestoComida> recomendar(List<PuestoComida> puestos) {
    return puestos.stream()
      .filter(p -> p.getTipo().equalsIgnoreCase(tipo))
      .collect(Collectors.toList());
  }
}

