package teoremadelsabor.observer;

/**
 * Interfaz del patrón Observer.
 * Define las operaciones del sujeto observable.
 */
public interface Sujeto {
    void suscribir(Observador o);
    void notificar(String evento);
}

