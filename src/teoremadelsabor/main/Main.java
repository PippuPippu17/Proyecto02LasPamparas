package teoremadelsabor.main;

import teoremadelsabor.mvc.*;
import teoremadelsabor.persistencia.AlmacenPuestos;

/**
 * Clase principal del programa.
 */
public class Main {
  public static void main(String[] args) {
    GestorPuestos gestor = new GestorPuestos();
    ControladorPuestos controlador = new ControladorPuestos(gestor);
    Vista vista = new Vista(controlador);
    vista.menu();
  }
}

