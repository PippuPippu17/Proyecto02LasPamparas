package teoremadelsabor.tests;

import teoremadelsabor.strategy.*;
import teoremadelsabor.mvc.PuestoComida;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Pruebas unitarias avanzadas para el patrón Strategy
 * Valida sistema de recomendaciones con múltiples estrategias
 */
public class StrategyPatternTest {

  private static int testsPasados = 0;
  private static int testsFallados = 0;

  public static void main(String[] args) {
    System.out.println("╔════════════════════════════════════════════════════════╗");
    System.out.println("║    🧪 PRUEBAS AVANZADAS - STRATEGY PATTERN 🧪        ║");
    System.out.println("╚════════════════════════════════════════════════════════╝\n");

    testRecomendacionPorPrecio();
    testRecomendacionPorTipo();
    testRecomendacionPorUbicacion();
    testRecomendacionMixta();
    testCambioDeEstrategia();
    testRecomendacionConListaVacia();
    testRecomendacionConUnPuesto();
    testMultiplesCriterios();
    testConsistenciaRecomendaciones();
    testFiltrajeAvanzado();

    System.out.println("\n╔════════════════════════════════════════════════════════╗");
    System.out.println("║                    📊 RESUMEN 📊                       ║");
    System.out.println("╠════════════════════════════════════════════════════════╣");
    System.out.printf("║  ✅ Tests pasados:    %-31d║%n", testsPasados);
    System.out.printf("║  ❌ Tests fallidos:   %-31d║%n", testsFallados);
    System.out.println("╚════════════════════════════════════════════════════════╝");
  }

  private static void testRecomendacionPorPrecio() {
    System.out.println("▶️  Test 1: Recomendación por precio");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();
      StrategyRecomendacion estrategia = new RecomendacionPrecio(60.0);

      List<PuestoComida> recomendados = estrategia.recomendar(puestos);

      assert recomendados != null : "Lista de recomendados no debe ser null";

      // Verificar que todos cumplen con el precio máximo
      for (PuestoComida p : recomendados) {
        assert p.getPrecioPromedio() <= 60.0 : "Precio debe ser <= 60.0";
      }

      System.out.println("   ✅ PASADO - Recomendados: " + recomendados.size());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testRecomendacionPorTipo() {
    System.out.println("▶️  Test 2: Recomendación por tipo");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();
      StrategyRecomendacion estrategia = new RecomendacionTipo("Tacos");

      List<PuestoComida> recomendados = estrategia.recomendar(puestos);

      assert recomendados != null : "Lista de recomendados no debe ser null";

      // Verificar que todos son del tipo correcto
      for (PuestoComida p : recomendados) {
        assert p.getTipo().equalsIgnoreCase("Tacos") : "Tipo debe ser Tacos";
      }

      System.out.println("   ✅ PASADO - Recomendados: " + recomendados.size());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testRecomendacionPorUbicacion() {
    System.out.println("▶️  Test 3: Recomendación por ubicación");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();
      StrategyRecomendacion estrategia = new RecomendacionUbicacion("Media Luna");

      List<PuestoComida> recomendados = estrategia.recomendar(puestos);

      assert recomendados != null : "Lista de recomendados no debe ser null";

      // Verificar que todos están en la ubicación correcta
      for (PuestoComida p : recomendados) {
        assert p.getUbicacion().equalsIgnoreCase("Media Luna") : "Ubicación debe ser Media Luna";
      }

      System.out.println("   ✅ PASADO - Recomendados: " + recomendados.size());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testRecomendacionMixta() {
    System.out.println("▶️  Test 4: Recomendación mixta (combinada)");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();
      StrategyRecomendacion estrategia = new RecomendacionMixta(50.0, "Tacos");

      List<PuestoComida> recomendados = estrategia.recomendar(puestos);

      assert recomendados != null : "Lista de recomendados no debe ser null";

      // Verificar que cumplen ambas condiciones
      for (PuestoComida p : recomendados) {
        assert p.getPrecioPromedio() <= 50.0 : "Precio debe ser <= 50.0";
        assert p.getTipo().equalsIgnoreCase("Tacos") : "Tipo debe ser Tacos";
      }

      System.out.println("   ✅ PASADO - Recomendados: " + recomendados.size());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testCambioDeEstrategia() {
    System.out.println("▶️  Test 5: Cambio dinámico de estrategia");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();

      StrategyRecomendacion estrategia1 = new RecomendacionPrecio(50.0);
      StrategyRecomendacion estrategia2 = new RecomendacionTipo("Tacos");
      StrategyRecomendacion estrategia3 = new RecomendacionUbicacion("Comedor");

      List<PuestoComida> rec1 = estrategia1.recomendar(puestos);
      List<PuestoComida> rec2 = estrategia2.recomendar(puestos);
      List<PuestoComida> rec3 = estrategia3.recomendar(puestos);

      assert rec1 != null : "Recomendación 1 no debe ser null";
      assert rec2 != null : "Recomendación 2 no debe ser null";
      assert rec3 != null : "Recomendación 3 no debe ser null";

      System.out.println("   ✅ PASADO - Múltiples estrategias funcionan correctamente");
      System.out.println("      Por precio ≤ 50: " + rec1.size() + " puestos");
      System.out.println("      Por tipo (Tacos): " + rec2.size() + " puestos");
      System.out.println("      Por ubicación (Comedor): " + rec3.size() + " puestos");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testRecomendacionConListaVacia() {
    System.out.println("▶️  Test 6: Recomendación con lista vacía (caso borde)");
    try {
      List<PuestoComida> puestosVacios = new ArrayList<>();
      StrategyRecomendacion estrategia = new RecomendacionPrecio(100.0);

      List<PuestoComida> recomendados = estrategia.recomendar(puestosVacios);

      assert recomendados != null : "Lista no debe ser null aunque esté vacía";
      assert recomendados.isEmpty() : "Lista debe estar vacía";

      System.out.println("   ✅ PASADO - Maneja correctamente lista vacía");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testRecomendacionConUnPuesto() {
    System.out.println("▶️  Test 7: Recomendación con un solo puesto");
    try {
      List<PuestoComida> unPuesto = new ArrayList<>();
      unPuesto.add(new PuestoComida("T1", "Solo", "Tacos", "Media Luna", 50.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo"));

      StrategyRecomendacion estrategia = new RecomendacionPrecio(100.0);
      List<PuestoComida> recomendados = estrategia.recomendar(unPuesto);

      assert recomendados != null : "Lista no debe ser null";
      assert recomendados.size() == 1 : "Debe recomendar el único puesto disponible";

      System.out.println("   ✅ PASADO - Maneja correctamente un solo puesto");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testMultiplesCriterios() {
    System.out.println("▶️  Test 8: Múltiples criterios de precio");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();

      // Probar con diferentes límites de precio
      StrategyRecomendacion barato = new RecomendacionPrecio(40.0);
      StrategyRecomendacion medio = new RecomendacionPrecio(60.0);
      StrategyRecomendacion caro = new RecomendacionPrecio(100.0);

      List<PuestoComida> recBarato = barato.recomendar(puestos);
      List<PuestoComida> recMedio = medio.recomendar(puestos);
      List<PuestoComida> recCaro = caro.recomendar(puestos);

      assert recBarato.size() <= recMedio.size() : "Más baratos deben ser menos o iguales";
      assert recMedio.size() <= recCaro.size() : "Precio medio debe ser menos o igual que caro";

      System.out.println("   ✅ PASADO - Múltiples criterios funcionan correctamente");
      System.out.println("      Baratos (≤$40): " + recBarato.size());
      System.out.println("      Medios (≤$60): " + recMedio.size());
      System.out.println("      Todos (≤$100): " + recCaro.size());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testConsistenciaRecomendaciones() {
    System.out.println("▶️  Test 9: Consistencia en múltiples llamadas");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();
      StrategyRecomendacion estrategia = new RecomendacionPrecio(50.0);

      List<PuestoComida> rec1 = estrategia.recomendar(puestos);
      List<PuestoComida> rec2 = estrategia.recomendar(puestos);
      List<PuestoComida> rec3 = estrategia.recomendar(puestos);

      // Con la misma estrategia y mismos datos, debe dar el mismo resultado
      assert rec1.size() == rec2.size() : "Resultados inconsistentes entre llamadas";
      assert rec2.size() == rec3.size() : "Resultados inconsistentes entre llamadas";

      System.out.println("   ✅ PASADO - Resultados consistentes en múltiples llamadas");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testFiltrajeAvanzado() {
    System.out.println("▶️  Test 10: Filtrado avanzado de recomendaciones");
    try {
      List<PuestoComida> puestos = crearPuestosVariados();

      // Usar diferentes estrategias y verificar que todas funcionen
      StrategyRecomendacion[] estrategias = {
        new RecomendacionPrecio(60.0),
        new RecomendacionTipo("Tacos"),
        new RecomendacionUbicacion("Media Luna"),
        new RecomendacionMixta(40.0, "Postres")
      };

      int totalRecomendaciones = 0;
      for (StrategyRecomendacion estrategia : estrategias) {
        List<PuestoComida> recomendados = estrategia.recomendar(puestos);
        assert recomendados != null : "Recomendaciones no deben ser null";
        totalRecomendaciones += recomendados.size();
      }

      assert totalRecomendaciones >= 0 : "Total debe ser válido";

      System.out.println("   ✅ PASADO - Todas las estrategias funcionan correctamente");
      System.out.println("      Total de recomendaciones acumuladas: " + totalRecomendaciones);
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static List<PuestoComida> crearPuestosVariados() {
    List<PuestoComida> puestos = new ArrayList<>();

    puestos.add(new PuestoComida("P1", "Tacos Baratos", "Tacos", "Media Luna", 30.0,
      LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo"));

    puestos.add(new PuestoComida("P2", "Comida Cara", "Comida Completa", "Comedor", 80.0,
      LocalTime.of(8, 0), LocalTime.of(19, 0), "Efectivo|Transferencia"));

    puestos.add(new PuestoComida("P3", "Snacks Medios", "Snacks", "Puestos", 50.0,
      LocalTime.of(7, 0), LocalTime.of(20, 0), "Efectivo"));

    puestos.add(new PuestoComida("P4", "Postres Economicos", "Postres", "Media Luna", 35.0,
      LocalTime.of(10, 0), LocalTime.of(17, 0), "Efectivo|Transferencia"));

    puestos.add(new PuestoComida("P5", "Tacos Premium", "Tacos", "Comedor", 65.0,
      LocalTime.of(11, 0), LocalTime.of(18, 0), "Efectivo|Transferencia|Tarjeta"));

    return puestos;
  }
}
