package teoremadelsabor.composite;

import teoremadelsabor.mvc.PuestoComida;

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
}

