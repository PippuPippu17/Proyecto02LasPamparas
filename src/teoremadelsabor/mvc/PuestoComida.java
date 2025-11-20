package teoremadelsabor.mvc;

import teoremadelsabor.observer.Observador;
import teoremadelsabor.observer.Sujeto;
import teoremadelsabor.state.EstadoPuesto;
import teoremadelsabor.state.EstadoAbierto;
import teoremadelsabor.state.EstadoCerrado;
import teoremadelsabor.state.EstadoEnDescanso;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


/**
 * Clase que representa un puesto de comida
 * Implementa el patron State para gestionar estados (Abierto/Cerrado/EnDescanso)
 * Implementa el patron Observer como Sujeto que notifica cambios a observadores
 */
public class PuestoComida implements Sujeto {

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
    if (!observadores.contains(obs)) {
      observadores.add(obs);
      System.out.println(obs + " se ha suscrito a " + nombre);
    }
  }


  /**
   * Desuscribe un observador
   * @param obs Observador a desuscribir del puesto
   */
  public void desuscribir(Observador obs) {
    if (observadores.remove(obs)) {
      System.out.println(obs + " se ha desuscrito de " + nombre);
    }
  }


  /**
   * Notifica a observadores
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
    EstadoPuesto estadoAnterior = estado;

    // Verificar si esta fuera del horario de operacion
    if (hora.isBefore(horaApertura) || hora.isAfter(horaCierre)) {
      estado = new EstadoCerrado();
    }
    // Verificar si esta en horario de descanso (15:00 - 16:00)
    else if (hora.isAfter(LocalTime.of(15, 0)) && hora.isBefore(LocalTime.of(16, 0))) {
      estado = new EstadoEnDescanso();
    }
    // Esta en horario de operacion normal
    else {
      estado = new EstadoAbierto();
    }

    // Notificar solo si el estado cambio
    if (!estado.getClass().equals(estadoAnterior.getClass())) {
      notificar("El puesto ahora esta: " + estado.getNombre());
    }
  }


  /**
   * Cambia manualmente el estado del puesto
   * @param nuevoEstado El nuevo estado a asignar
   */
  public void cambiarEstado(EstadoPuesto nuevoEstado) {
    if (!estado.getClass().equals(nuevoEstado.getClass())) {
      estado = nuevoEstado;
      notificar("El puesto cambio a: " + estado.getNombre());
    }
  }


  /**
   * Pone el puesto en descanso temporalmente
   */
  public void irADescanso() {
    cambiarEstado(new EstadoEnDescanso());
  }


  /**
   * Abre el puesto manualmente
   */
  public void abrir() {
    cambiarEstado(new EstadoAbierto());
  }


  /**
   * Cierra el puesto manualmente
   */
  public void cerrar() {
    cambiarEstado(new EstadoCerrado());
  }


  /**
   * Actualiza el precio promedio del puesto y notifica a los observadores
   * @param nuevoPrecio Nuevo precio promedio
   */
  public void actualizarPrecio(double nuevoPrecio) {
    if (this.precioPromedio != nuevoPrecio) {
      double precioAnterior = this.precioPromedio;
      this.precioPromedio = nuevoPrecio;
      notificar("Precio actualizado de $" + precioAnterior + " a $" + nuevoPrecio);
    }
  }


  /**
   * Actualiza el tipo de comida (menu) y notifica a los observadores
   * @param nuevoTipo Nuevo tipo de comida
   */
  public void actualizarMenu(String nuevoTipo) {
    if (!this.tipo.equals(nuevoTipo)) {
      String tipoAnterior = this.tipo;
      this.tipo = nuevoTipo;
      notificar("Menu actualizado de '" + tipoAnterior + "' a '" + nuevoTipo + "'");
    }
  }


  /**
   * Actualiza los metodos de pago y notifica a los observadores
   * @param nuevosMetodos Nuevos metodos de pago
   */
  public void actualizarMetodosPago(String nuevosMetodos) {
    if (!this.metodoPago.equals(nuevosMetodos)) {
      this.metodoPago = nuevosMetodos;
      notificar("Metodos de pago actualizados a: " + nuevosMetodos);
    }
  }
}

