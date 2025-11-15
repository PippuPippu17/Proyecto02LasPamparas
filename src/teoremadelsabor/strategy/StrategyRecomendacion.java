package teoremadelsabor.strategy;

import java.util.List;
import teoremadelsabor.mvc.PuestoComida;

/**
 * Interfaz para comportamiento de recomendacion de puestos de comida
 */
public interface StrategyRecomendacion {
    /**
     * Lista de puestos recomendados dependiendo la estrategia.
     * @param puestos Lista de todos los puestos disponibles.
     * @return Lista de puestos recomendados.
     */
    List<PuestoComida> recomendar(List<PuestoComida> puestos);
}

