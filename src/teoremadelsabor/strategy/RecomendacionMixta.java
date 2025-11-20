package teoremadelsabor.strategy;

import java.util.List;
import java.util.stream.Collectors;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Estrategia de recomendacion mixta que combina multiples criterios
 * Filtra por precio maximo y tipo de comida simultaneamente
 */
public class RecomendacionMixta implements StrategyRecomendacion {

  private double precioMax;
  private String tipoPreferido;

  /**
   * Crea una recomendacion mixta combinando precio y tipo
   * @param precioMax Precio maximo aceptable
   * @param tipoPreferido Tipo de comida preferido
   */
  public RecomendacionMixta(double precioMax, String tipoPreferido) {
    this.precioMax = precioMax;
    this.tipoPreferido = tipoPreferido;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PuestoComida> recomendar(List<PuestoComida> puestos) {
    return puestos.stream()
      .filter(p -> p.getPrecioPromedio() <= precioMax)
      .filter(p -> p.getTipo().equalsIgnoreCase(tipoPreferido))
      .collect(Collectors.toList());
  }
}
