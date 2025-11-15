package teoremadelsabor.strategy; 
import java.util.List; 
import java.util.stream.Collectors; 
import teoremadelsabor.mvc.PuestoComida; 
/** * Estrategia de recomendación basada en precio promedio del puesto. */ 
public class RecomendacionPrecio implements StrategyRecomendacion { 
  private double precioMax; 

  /** 
   * Crea una recomendacion por precio maximo. 
   * @param precioMax Precio maximo 
   */ 
  public RecomendacionPrecio(double precioMax) { 
    this.precioMax = precioMax; 
  } 

  /** 
   * {@inheritDoc} 
   */  
  @Override public List<PuestoComida> recomendar(List<PuestoComida> puestos) { 
    return puestos.stream() 
    .filter(p -> p.getPrecioPromedio() <= precioMax) 
    .collect(Collectors.toList());
  }
}
