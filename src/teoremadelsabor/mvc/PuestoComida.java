package teoremadelsabor.mvc;

import teoremadelsabor.observer.Observador;
import teoremadelsabor.state.EstadoPuesto;
import teoremadelsabor.state.EstadoAbierto;
import teoremadelsabor.state.EstadoCerrado;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


/**
 * Clase que representa un puesto de comida
 * Implementa State y Observer
 */
public class PuestoComida {

  private String id;
  private String nombre;
  private String tipo;
  private String ubicacion;
  private double precioPromedio;
  private LocalTime horaApertura;
  private LocalTime horaCierre;
  private EstadoPuesto estado;
  private String metodoPago; 
  private final List<Observador> observadores;

  /**
   * Constructor del puesto de comida (sus datos)
   * @param id ID del puesto
   * @param nombre Nombre del puesto de comida
   * @param tipo Tipo de comida del puesto
   * @param ubicacion Ubicacion del puesto dentro de la facultad
   * @param precioPromedio Precio promedio de los productos del puesto
   * @param horaApertura Hora de apertura del puesto
   * @param horaCierre Hora de cierre del puesto
   * @param metodoPago Metodo de pago que acepta el puesto
   */
  public PuestoComida(String id, String nombre, String tipo, String ubicacion,
    double precioPromedio, LocalTime horaApertura, LocalTime horaCierre, String metodoPago) {
        
    this.id = id;
    this.nombre = nombre;
    this.tipo = tipo;
    this.ubicacion = ubicacion;
    this.precioPromedio = precioPromedio;
    this.horaApertura = horaApertura;
    this.horaCierre = horaCierre;
    this.metodoPago = metodoPago;
    this.estado = new EstadoCerrado(); 
    this.observadores = new ArrayList<>();
  }

  /**
   * Devuelve el ID del puesto de comida
   * @return ID del puesto
   */
  public String getId() { 
    return id; 
  }


  /**
   * Devuelve el nombre del puesto de comida
   * @return Nombre del puesto
   */
  public String getNombre() { 
    return nombre; 
  }


  /**
   * Devuelve el tipo de comida del puesto
   * @return Tipo de comida del puesto
   */
  public String getTipo() { 
    return tipo; 
  }


  /**
   * Devuelve el precio promedio de los productos del puesto
   *
   * @return Precio promedio del puesto
   */
  public double getPrecioPromedio() { 
    return precioPromedio; 
  }


  /**
   * Devuelve la ubicación del puesto dentro de la Fac 
   * @return Ubicacion del puesto
   */
  public String getUbicacion() { 
    return ubicacion; 
  }


  /**
   * Devuelve la hora de apertura del puesto
   * @return Hora de apertura del puesto
   */
  public LocalTime getHoraApertura() { 
    return horaApertura; 
  }

  /**
   * Devuelve la hora de cierre del puesto
   * @return Hora de cierre del puesto
   */
  public LocalTime getHoraCierre() { 
    return horaCierre; 
  }

  /**
   * Devuelve el estado actual del puesto (abierto, cerrado, en descanso)
   * @return Estado del puesto
   */
  public EstadoPuesto getEstado() { 
    return estado; 
  }

  /**
   * Devuelve el metodo de pago que aceota el puesto 
   * Efectivo, transferencia, tarjeta (uno o multiples)
   * @return Metodo de pago aceptado por el puesto
   */
  public String getMetodosPago() {
    return metodoPago;
  }


  /**
   * Muestra la información del puesto en dos líneas compactas.
   */
  public void mostrarInfo() {
    // Línea 1: id, nombre, tipo, precio, ubicación, estado
    System.out.printf("%s. %s (%s) • $%.2f • %s [%s]%n",
      id, nombre, tipo, precioPromedio, ubicacion, estado.getClass().getSimpleName()
    );

    // Horarios y metodos de pago 
    System.out.printf("  Horario: %s-%s • Pago: %s%n%n",
      horaApertura, horaCierre, abreviarMetodosPago(3)
    );
  }
 
  /** 
   * Metodo auxiliar para imprimir los metodos de pago
   * que aceptan los puestos de comida
   * @param limite Numero de metodos de pago maximo aceptados
   */
  private String abreviarMetodosPago(int limite) {
    if (metodoPago == null || metodoPago.isBlank()) return "NA";
    String[] m = metodoPago.split("\\|");
    if (m.length <= limite) return String.join(", ", m);
    String[] primeros = Arrays.copyOfRange(m, 0, limite);
    return String.join(", ", primeros) + ", +" + (m.length - limite);
  }


  /**
   * Suscribe un observador 
   * @param obs Observador (cliente) a suscribir al puesto
   */
  public void suscribir(Observador obs) {
    observadores.add(obs);
  }


  /** 
   * Notifica a bservadores
   * @param notif Notificacion (mensaje) a enviar a observador(es) suscrito(s)
   */
  public void notificar(String notif) {
    for (Observador obs : observadores) {
      obs.actualizar(this, notif);
    }
  }


  /** 
   * Actualiza el estado del puesto con base en la hora
   * @param hora Hora actual de CMDX (LocalTime)
   */
  public void actualizarEstadoPorHora(LocalTime hora) {
    boolean abierto = hora.isAfter(horaApertura) && hora.isBefore(horaCierre);
    if (abierto && !(estado instanceof EstadoAbierto)) {
      estado = new EstadoAbierto();
      notificar("El puesto esta abierto");
    } else if (!abierto && !(estado instanceof EstadoCerrado)) {
      estado = new EstadoCerrado();
      notificar("El puesto esta cerrado");
    }
  }
}

