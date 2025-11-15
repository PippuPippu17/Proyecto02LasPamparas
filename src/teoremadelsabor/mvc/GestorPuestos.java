package teoremadelsabor.mvc;

import teoremadelsabor.persistencia.AlmacenPuestos;
import teoremadelsabor.strategy.StrategyRecomendacion;

import java.time.LocalTime;
import java.util.List;

/**
 * Gestiona PuestoComida y coordina las operaciones
 * relacionadas con persistencia, busqueda, actualizacion de estados y
 * recomendaciones
 */
public class GestorPuestos {

  /** 
   * Lista de puestos de comida administrados por el sistema. 
   */
  private List<PuestoComida> puestos;

  /**
   * Crea un gestor de puestos e inicializa la lista cargandola desde la
   * persistencia.
   */
  public GestorPuestos() {
    this.puestos = AlmacenPuestos.cargarPuestos();
  }


  /**
   * Obtiene la lista completa de puestos administrados.
   *
   * @return lista de PuestoComida ya cargadoss
   */
  public List<PuestoComida> getPuestos() {
    return puestos;
  }


  /**
   * Agrega un nuevo puesto a la lista y actualiza el archivo de los puestos
   * @param puesto el puesto de comida a agregar a la colección.
   */
  public void agregarPuesto(PuestoComida puesto) {
    puestos.add(puesto);
    AlmacenPuestos.guardarPuestos(puestos);
  }


  /**
   * Actualiza el estado de los puestos segun la hora
   *
   * @param hora la hora actual que determina estados de los puestos
   */
  public void actualizarEstados(LocalTime hora) {
    for (PuestoComida p : puestos) {
      p.actualizarEstadoPorHora(hora);
    }
    AlmacenPuestos.guardarPuestos(puestos);
  }


  /**
   * Busca los puestos que se encuentran abiertos para la hora dada
   *
   * @param hora HOra para ver que puestos estan abiertos y cuales no]  
   * @return lista de Puestos que abiertos a esa hora
   */
  public List<PuestoComida> buscarAbierto(LocalTime hora) {
    return puestos.stream()
      .filter(p -> p.getEstado().disponibilidad(hora))
      .toList();
  }


  /**
   * Aplica una estrategia de recomendacion basada en precio, tipo de comida, ubicación 
   * @param recomendacion la estrategia a usar
   * @return Puestos recomendados que cumplan la caracteristica
   */
  public List<PuestoComida> recomendar(StrategyRecomendacion recomendacion) {
    return recomendacion.recomendar(puestos);  
  }
}

