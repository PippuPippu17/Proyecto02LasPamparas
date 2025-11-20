package teoremadelsabor.observer;

/**
 * Interfaz Sujeto del patron Observer
 * Define las operaciones para gestionar observadores
 */
public interface Sujeto {

  /**
   * Suscribe un observador al sujeto
   * @param obs Observador a suscribir
   */
  void suscribir(Observador obs);

  /**
   * Desuscribe un observador del sujeto
   * @param obs Observador a desuscribir
   */
  void desuscribir(Observador obs);

  /**
   * Notifica a todos los observadores suscritos
   * @param notificacion Mensaje de notificacion
   */
  void notificar(String notificacion);
}
