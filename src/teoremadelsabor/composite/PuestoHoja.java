package teoremadelsabor.composite;

import teoremadelsabor.mvc.PuestoComida;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un puesto individual (hoja en patron Composite)
 */
public class PuestoHoja implements ComponenteZona {
  private PuestoComida puesto;

  /**
   * Crea un nuevo puesto hoja
   * @param puesto Puesto de comida
   */
  public PuestoHoja(PuestoComida puesto) {
    this.puesto = puesto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mostrar() {
    puesto.mostrarInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PuestoComida> listarAbiertos(LocalTime hora) {
    List<PuestoComida> resultado = new ArrayList<>();
    if (puesto.getEstado().disponibilidad(hora)) {
      resultado.add(puesto);
    }
    return resultado;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int contarPuestos() {
    return 1;
  }

  /**
   * Obtiene el puesto de comida envuelto
   * @return PuestoComida
   */
  public PuestoComida getPuesto() {
    return puesto;
  }
}

