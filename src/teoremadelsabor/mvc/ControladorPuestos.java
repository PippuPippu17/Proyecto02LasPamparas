package teoremadelsabor.mvc;
import teoremadelsabor.strategy.StrategyRecomendacion;
import teoremadelsabor.composite.Zona;
import teoremadelsabor.observer.Usuario;
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

  /**
   * Muestra los puestos organizados por zonas usando Composite
   */
  public void mostrarPorZonas() {
    Zona facultad = gestor.crearEstructuraZonas();
    facultad.mostrar();
  }

  /**
   * Muestra solo los puestos abiertos en este momento por zonas
   */
  public void mostrarAbiertosPorZona() {
    Zona facultad = gestor.crearEstructuraZonas();
    LocalTime ahora = LocalTime.now();
    System.out.println("\n=== PUESTOS ABIERTOS AHORA (" + ahora + ") ===");
    facultad.listarAbiertos(ahora).forEach(PuestoComida::mostrarInfo);
  }

  /**
   * Suscribe un usuario a un puesto especifico
   * @param nombreUsuario Nombre del usuario
   * @param idPuesto ID del puesto
   * @return true si se suscribio exitosamente, false si no
   */
  public boolean suscribirUsuario(String nombreUsuario, String idPuesto) {
    PuestoComida puesto = gestor.buscarPorId(idPuesto);
    if (puesto != null) {
      Usuario usuario = new Usuario(nombreUsuario);
      puesto.suscribir(usuario);
      return true;
    }
    return false;
  }

  /**
   * Cambia manualmente el estado de un puesto
   * @param idPuesto ID del puesto
   * @param accion "abrir", "cerrar" o "descanso"
   */
  public void cambiarEstadoPuesto(String idPuesto, String accion) {
    PuestoComida puesto = gestor.buscarPorId(idPuesto);
    if (puesto != null) {
      switch (accion.toLowerCase()) {
        case "abrir":
          puesto.abrir();
          System.out.println("Puesto " + puesto.getNombre() + " abierto manualmente");
          break;
        case "cerrar":
          puesto.cerrar();
          System.out.println("Puesto " + puesto.getNombre() + " cerrado manualmente");
          break;
        case "descanso":
          puesto.irADescanso();
          System.out.println("Puesto " + puesto.getNombre() + " en descanso");
          break;
        default:
          System.out.println("Accion no valida");
      }
    } else {
      System.out.println("Puesto no encontrado");
    }
  }
}

