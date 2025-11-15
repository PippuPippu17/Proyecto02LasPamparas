package teoremadelsabor.mvc;
import teoremadelsabor.strategy.StrategyRecomendacion;
import java.time.LocalTime;

/**
 * Controlador (Intermediario) que conecta la vista con el modelo
 */
public class ControladorPuestos {
  
  private GestorPuestos gestor;

  /**
   * Crea el controlador
   * @param gestor El gestor de puestos.
   */
  public ControladorPuestos(GestorPuestos gestor) {
    this.gestor = gestor;
  }

  /**
   * Devuelve gestor de puestos
   * @return El gestor de puestos.
   */
  public GestorPuestos getGestor() {
    return gestor;
  }

  /**
   * Muestra puestos
   */
  public void mostrarPuestos() {
    gestor.getPuestos().forEach(PuestoComida::mostrarInfo);
  }

  /**
   * Actualiza el estado de los puestos conforme la hora actual
   */
  public void actualizarSegunHora() {
    gestor.actualizarEstados(LocalTime.now());
  }

  /**
   * Recomienda puestos conforme  estrategia
   * @param estrategia Estrategia de recomendacion.
   */
  public void recomendar(StrategyRecomendacion estrategia) {
    System.out.println("Recomendaciones:");
    gestor.recomendar(estrategia).forEach(PuestoComida::mostrarInfo);
  }
}

