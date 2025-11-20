package teoremadelsabor.composite;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import teoremadelsabor.mvc.PuestoComida;

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
   * Obtiene el nombre de la zona
   * @return Nombre de la zona
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Agrega un componente a la zona (puede ser un puesto o subzona).
   * @param componente Componente a agregar.
   */
  public void agregar(ComponenteZona componente) {
    componentes.add(componente);
  }

  /**
   * Remueve un componente de la zona
   * @param componente Componente a remover
   */
  public void remover(ComponenteZona componente) {
    componentes.remove(componente);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mostrar() {
    System.out.println("\n=== ZONA: " + nombre + " ===");
    System.out.println("Total de puestos: " + contarPuestos());
    System.out.println();
    for (ComponenteZona c : componentes) {
      c.mostrar();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PuestoComida> listarAbiertos(LocalTime hora) {
    List<PuestoComida> abiertos = new ArrayList<>();
    for (ComponenteZona c : componentes) {
      abiertos.addAll(c.listarAbiertos(hora));
    }
    return abiertos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int contarPuestos() {
    int total = 0;
    for (ComponenteZona c : componentes) {
      total += c.contarPuestos();
    }
    return total;
  }

  /**
   * Obtiene todos los componentes de esta zona
   * @return Lista de componentes
   */
  public List<ComponenteZona> getComponentes() {
    return new ArrayList<>(componentes);
  }
}

