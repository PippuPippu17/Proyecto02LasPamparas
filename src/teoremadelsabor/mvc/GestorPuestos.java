package teoremadelsabor.mvc;

import teoremadelsabor.persistencia.AlmacenPuestos;
import teoremadelsabor.strategy.StrategyRecomendacion;
import teoremadelsabor.composite.*;

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


  /**
   * Crea la estructura de zonas con todos los puestos organizados
   * @return Zona raiz con todas las zonas de la facultad
   */
  public Zona crearEstructuraZonas() {
    Zona facultad = new Zona("Facultad de Ciencias");

    // Crear zonas principales
    Zona mediaLuna = new Zona("Media Luna");
    Zona comedor = new Zona("Comedor");
    Zona estacionamiento = new Zona("Estacionamiento");
    Zona puestosExteriores = new Zona("Puestos Exteriores");
    Zona edificios = new Zona("Edificios");

    // Agregar puestos a cada zona segun su ubicacion
    for (PuestoComida p : puestos) {
      PuestoHoja hoja = new PuestoHoja(p);

      switch (p.getUbicacion()) {
        case "Media Luna":
          mediaLuna.agregar(hoja);
          break;
        case "Comedor":
          comedor.agregar(hoja);
          break;
        case "Estacionamiento":
          estacionamiento.agregar(hoja);
          break;
        case "Puestos":
          puestosExteriores.agregar(hoja);
          break;
        default:
          if (p.getUbicacion().contains("Edificio")) {
            edificios.agregar(hoja);
          }
          break;
      }
    }

    // Agregar todas las zonas a la facultad
    facultad.agregar(mediaLuna);
    facultad.agregar(comedor);
    facultad.agregar(estacionamiento);
    facultad.agregar(puestosExteriores);
    facultad.agregar(edificios);

    return facultad;
  }


  /**
   * Busca un puesto por su ID
   * @param id ID del puesto a buscar
   * @return PuestoComida si existe, null si no
   */
  public PuestoComida buscarPorId(String id) {
    return puestos.stream()
      .filter(p -> p.getId().equals(id))
      .findFirst()
      .orElse(null);
  }
}

