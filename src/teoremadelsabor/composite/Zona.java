package teoremadelsabor.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Zona de la facultad donde se agrupan varios puestos.
 */
public class Zona implements ComponenteZona {
    
  private String nombre;
  private List<ComponenteZona> componentes;

  /**
   * Crea una nueva zona de puestos.
   * @param nombre Nombre de la zona.
   */
  public Zona(String nombre) {
    this.nombre = nombre;
    this.componentes = new ArrayList<>();
  }

  /**
   * Agrega un componente a la zona (puede ser un puesto o subzona).
   * @param componente Componente a agregar.
   */
  public void agregar(ComponenteZona componente) {
    componentes.add(componente);
  }

  /** 
   * {@inheritDoc} 
   */
  @Override
  public void mostrar() {
  System.out.println("Zona: " + nombre);
  for (ComponenteZona c : componentes) {
    c.mostrar();
    }
  }
}

