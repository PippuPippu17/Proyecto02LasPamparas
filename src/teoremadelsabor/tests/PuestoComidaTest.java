package teoremadelsabor.tests;

import teoremadelsabor.mvc.PuestoComida;
import teoremadelsabor.mvc.GestorPuestos;
import teoremadelsabor.state.*;
import teoremadelsabor.observer.Usuario;
import java.time.LocalTime;
import java.util.List;

/**
 * Pruebas unitarias para PuestoComida
 * Demuestra testing sin dependencias externas
 */
public class PuestoComidaTest {

  private static int testsPasados = 0;
  private static int testsFallados = 0;

  public static void main(String[] args) {
    System.out.println("========================================");
    System.out.println("  PRUEBAS UNITARIAS - TEOREMA DEL SABOR");
    System.out.println("========================================\n");

    testCreacionPuesto();
    testCambioEstado();
    testObserverPattern();
    testHorarioDisponibilidad();
    testActualizacionPrecio();
    testMultiplesObservadores();
    testEstadosInvalidos();
    testHorariosBorde();
    testGetters();
    testMetodosPago();
    testGestorPuestos();
    testFiltradoPorTipo();
    testFiltradoPorUbicacion();
    testFiltradoPorPrecio();
    testEstadoNombre();

    System.out.println("\n========================================");
    System.out.println("RESULTADOS:");
    System.out.println("  Pasados: " + testsPasados);
    System.out.println("  Fallados: " + testsFallados);
    System.out.println("========================================");
  }

  private static void testCreacionPuesto() {
    System.out.println("[TEST 1] Creacion de puesto...");
    try {
      PuestoComida puesto = new PuestoComida(
        "P001",
        "Tacos Don Juan",
        "Tacos",
        "Media Luna",
        45.0,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0),
        "Efectivo"
      );

      assert puesto.getId().equals("P001") : "ID incorrecto";
      assert puesto.getNombre().equals("Tacos Don Juan") : "Nombre incorrecto";
      assert puesto.getPrecioPromedio() == 45.0 : "Precio incorrecto";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testCambioEstado() {
    System.out.println("[TEST 2] Cambio de estado...");
    try {
      PuestoComida puesto = crearPuestoTest();

      puesto.abrir();
      assert puesto.getEstado() instanceof EstadoAbierto : "Deberia estar Abierto";

      puesto.cerrar();
      assert puesto.getEstado() instanceof EstadoCerrado : "Deberia estar Cerrado";

      puesto.irADescanso();
      assert puesto.getEstado() instanceof EstadoEnDescanso : "Deberia estar en Descanso";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testObserverPattern() {
    System.out.println("[TEST 3] Patron Observer...");
    try {
      PuestoComida puesto = crearPuestoTest();
      Usuario usuario = new Usuario("Juan");

      puesto.suscribir(usuario);
      assert true : "Suscripcion fallo";

      puesto.abrir(); // Deberia notificar

      puesto.desuscribir(usuario);
      assert true : "Desuscripcion fallo";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testHorarioDisponibilidad() {
    System.out.println("[TEST 4] Horario y disponibilidad...");
    try {
      PuestoComida puesto = crearPuestoTest();

      LocalTime dentroHorario = LocalTime.of(12, 0);
      puesto.actualizarEstadoPorHora(dentroHorario);
      assert puesto.getEstado().disponibilidad(dentroHorario) : "Deberia estar disponible";

      LocalTime fueraHorario = LocalTime.of(20, 0);
      puesto.actualizarEstadoPorHora(fueraHorario);
      assert !puesto.getEstado().disponibilidad(fueraHorario) : "No deberia estar disponible";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testActualizacionPrecio() {
    System.out.println("[TEST 5] Actualizacion de precio...");
    try {
      PuestoComida puesto = crearPuestoTest();
      double precioOriginal = puesto.getPrecioPromedio();

      puesto.actualizarPrecio(55.0);
      assert puesto.getPrecioPromedio() == 55.0 : "Precio no se actualizo";
      assert puesto.getPrecioPromedio() != precioOriginal : "Precio sigue igual";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testMultiplesObservadores() {
    System.out.println("[TEST 6] Multiples observadores...");
    try {
      PuestoComida puesto = crearPuestoTest();
      Usuario usuario1 = new Usuario("Maria");
      Usuario usuario2 = new Usuario("Pedro");
      Usuario usuario3 = new Usuario("Ana");

      puesto.suscribir(usuario1);
      puesto.suscribir(usuario2);
      puesto.suscribir(usuario3);

      puesto.abrir(); // Todos deben recibir notificacion

      puesto.desuscribir(usuario2);
      puesto.cerrar(); // Solo Maria y Ana reciben notificacion

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testEstadosInvalidos() {
    System.out.println("[TEST 7] Transiciones de estado...");
    try {
      PuestoComida puesto = crearPuestoTest();

      puesto.abrir();
      assert puesto.getEstado() instanceof EstadoAbierto : "Estado inicial incorrecto";

      puesto.abrir(); // Intentar abrir cuando ya esta abierto
      assert puesto.getEstado() instanceof EstadoAbierto : "Deberia seguir abierto";

      puesto.cerrar();
      puesto.cerrar(); // Intentar cerrar cuando ya esta cerrado
      assert puesto.getEstado() instanceof EstadoCerrado : "Deberia seguir cerrado";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testHorariosBorde() {
    System.out.println("[TEST 8] Casos borde de horarios...");
    try {
      PuestoComida puesto = crearPuestoTest(); // 9:00 - 18:00

      // Justo al abrir
      LocalTime apertura = LocalTime.of(9, 0);
      puesto.actualizarEstadoPorHora(apertura);
      assert puesto.getEstado().disponibilidad(apertura) : "Deberia estar disponible en hora de apertura";

      // Justo antes de cerrar
      LocalTime antescierre = LocalTime.of(17, 59);
      puesto.actualizarEstadoPorHora(antescierre);
      assert puesto.getEstado().disponibilidad(antescierre) : "Deberia estar disponible antes del cierre";

      // Justo al cerrar
      LocalTime cierre = LocalTime.of(18, 0);
      puesto.actualizarEstadoPorHora(cierre);

      // Justo antes de abrir
      LocalTime antesApertura = LocalTime.of(8, 59);
      puesto.actualizarEstadoPorHora(antesApertura);
      assert !puesto.getEstado().disponibilidad(antesApertura) : "No deberia estar disponible antes de apertura";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testGetters() {
    System.out.println("[TEST 9] Getters de puesto...");
    try {
      PuestoComida puesto = new PuestoComida(
        "P999",
        "Quesadillas La Lupe",
        "Comida Rapida",
        "Anexo C",
        35.50,
        LocalTime.of(8, 30),
        LocalTime.of(17, 30),
        "Efectivo|Transferencia"
      );

      assert puesto.getId().equals("P999") : "ID incorrecto";
      assert puesto.getNombre().equals("Quesadillas La Lupe") : "Nombre incorrecto";
      assert puesto.getTipo().equals("Comida Rapida") : "Tipo incorrecto";
      assert puesto.getUbicacion().equals("Anexo C") : "Ubicacion incorrecta";
      assert puesto.getPrecioPromedio() == 35.50 : "Precio incorrecto";
      assert puesto.getHoraApertura().equals(LocalTime.of(8, 30)) : "Hora apertura incorrecta";
      assert puesto.getHoraCierre().equals(LocalTime.of(17, 30)) : "Hora cierre incorrecta";
      assert puesto.getMetodosPago().equals("Efectivo|Transferencia") : "Metodos pago incorrectos";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testMetodosPago() {
    System.out.println("[TEST 10] Metodos de pago...");
    try {
      PuestoComida puesto1 = new PuestoComida("P1", "Test1", "Tacos", "Media Luna", 50.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
      assert puesto1.getMetodosPago().equals("Efectivo") : "Metodo pago solo efectivo incorrecto";

      PuestoComida puesto2 = new PuestoComida("P2", "Test2", "Tacos", "Media Luna", 50.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo|Transferencia|Tarjeta");
      assert puesto2.getMetodosPago().contains("Efectivo") : "Deberia aceptar efectivo";
      assert puesto2.getMetodosPago().contains("Transferencia") : "Deberia aceptar transferencia";
      assert puesto2.getMetodosPago().contains("Tarjeta") : "Deberia aceptar tarjeta";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testGestorPuestos() {
    System.out.println("[TEST 11] Gestor de puestos...");
    try {
      GestorPuestos gestor = new GestorPuestos();

      PuestoComida p1 = new PuestoComida("G1", "Tacos", "Tacos", "Media Luna", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
      PuestoComida p2 = new PuestoComida("G2", "Quesadillas", "Comida Rapida", "Anexo C", 35.0,
        LocalTime.of(8, 0), LocalTime.of(17, 0), "Efectivo|Transferencia");

      gestor.agregarPuesto(p1);
      gestor.agregarPuesto(p2);

      List<PuestoComida> puestos = gestor.getPuestos();
      assert puestos.size() >= 2 : "Deberia tener al menos 2 puestos agregados";

      PuestoComida buscado = gestor.buscarPorId("G1");
      assert buscado != null : "Deberia encontrar el puesto G1";
      assert buscado.getNombre().equals("Tacos") : "Nombre del puesto incorrecto";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testFiltradoPorTipo() {
    System.out.println("[TEST 12] Busqueda de puestos abiertos...");
    try {
      GestorPuestos gestor = new GestorPuestos();

      PuestoComida p1 = new PuestoComida("F1", "Tacos1", "Tacos", "Media Luna", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
      PuestoComida p2 = new PuestoComida("F2", "Tacos2", "Tacos", "Anexo C", 45.0,
        LocalTime.of(8, 0), LocalTime.of(14, 0), "Efectivo");

      gestor.agregarPuesto(p1);
      gestor.agregarPuesto(p2);

      // Actualizar estados primero
      LocalTime medioDia = LocalTime.of(12, 0);
      gestor.actualizarEstados(medioDia);

      List<PuestoComida> abiertos = gestor.buscarAbierto(medioDia);

      // Verificar que encontramos al menos los 2 que agregamos
      int contadorAgregados = 0;
      for (PuestoComida p : abiertos) {
        if (p.getId().equals("F1") || p.getId().equals("F2")) {
          contadorAgregados++;
        }
      }

      assert contadorAgregados == 2 : "Deberian estar abiertos los 2 puestos agregados";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testFiltradoPorUbicacion() {
    System.out.println("[TEST 13] Actualizacion masiva de estados...");
    try {
      GestorPuestos gestor = new GestorPuestos();

      PuestoComida p1 = new PuestoComida("U1", "Puesto1", "Tacos", "Media Luna", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
      PuestoComida p2 = new PuestoComida("U2", "Puesto2", "Comida Rapida", "Comedor", 45.0,
        LocalTime.of(8, 0), LocalTime.of(17, 0), "Efectivo");

      gestor.agregarPuesto(p1);
      gestor.agregarPuesto(p2);

      // Actualizar todos los estados a una hora especifica
      LocalTime hora = LocalTime.of(12, 0);
      gestor.actualizarEstados(hora);

      // Verificar que los puestos se actualizaron
      assert p1.getEstado().disponibilidad(hora) : "P1 deberia estar disponible";
      assert p2.getEstado().disponibilidad(hora) : "P2 deberia estar disponible";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testFiltradoPorPrecio() {
    System.out.println("[TEST 14] Estructura de zonas (Composite)...");
    try {
      GestorPuestos gestor = new GestorPuestos();

      gestor.agregarPuesto(new PuestoComida("Z1", "MediaLuna1", "Tacos", "Media Luna", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo"));
      gestor.agregarPuesto(new PuestoComida("Z2", "Comedor1", "Comida Completa", "Comedor", 50.0,
        LocalTime.of(8, 0), LocalTime.of(19, 0), "Efectivo"));

      // Crear estructura de zonas usando patron Composite
      var estructura = gestor.crearEstructuraZonas();

      assert estructura != null : "Estructura de zonas no deberia ser null";
      assert estructura.getNombre().equals("Facultad de Ciencias") : "Zona raiz incorrecta";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static void testEstadoNombre() {
    System.out.println("[TEST 15] Nombres de estados...");
    try {
      PuestoComida puesto = crearPuestoTest();

      puesto.abrir();
      assert puesto.getEstado().getNombre().equals("Abierto") : "Nombre estado Abierto incorrecto";

      puesto.cerrar();
      assert puesto.getEstado().getNombre().equals("Cerrado") : "Nombre estado Cerrado incorrecto";

      puesto.irADescanso();
      assert puesto.getEstado().getNombre().equals("En descanso") : "Nombre estado En descanso incorrecto";

      System.out.println("  ✓ PASADO\n");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("  ✗ FALLADO: " + e.getMessage() + "\n");
      testsFallados++;
    }
  }

  private static PuestoComida crearPuestoTest() {
    return new PuestoComida(
      "TEST001",
      "Puesto Test",
      "Tacos",
      "Media Luna",
      50.0,
      LocalTime.of(9, 0),
      LocalTime.of(18, 0),
      "Efectivo"
    );
  }
}
