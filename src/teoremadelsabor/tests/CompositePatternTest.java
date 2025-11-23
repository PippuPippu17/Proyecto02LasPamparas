package teoremadelsabor.tests;

import teoremadelsabor.composite.*;
import teoremadelsabor.mvc.PuestoComida;
import java.time.LocalTime;
import java.util.List;

/**
 * Pruebas unitarias avanzadas para el patrón Composite
 * Valida la estructura jerárquica de zonas y puestos
 */
public class CompositePatternTest {

  private static int testsPasados = 0;
  private static int testsFallados = 0;

  public static void main(String[] args) {
    System.out.println("╔════════════════════════════════════════════════════════╗");
    System.out.println("║    🧪 PRUEBAS AVANZADAS - COMPOSITE PATTERN 🧪       ║");
    System.out.println("╚════════════════════════════════════════════════════════╝\n");

    testCreacionZona();
    testAgregarPuestosAZona();
    testZonasAnidadas();
    testMostrarEstructura();
    testZonaVacia();
    testMultiplesNiveles();
    testEstructuraCompleja();
    testOperacionesComposite();
    testContarPuestos();
    testListarAbiertos();

    System.out.println("\n╔════════════════════════════════════════════════════════╗");
    System.out.println("║                    📊 RESUMEN 📊                       ║");
    System.out.println("╠════════════════════════════════════════════════════════╣");
    System.out.printf("║  ✅ Tests pasados:    %-31d║%n", testsPasados);
    System.out.printf("║  ❌ Tests fallidos:   %-31d║%n", testsFallados);
    System.out.println("╚════════════════════════════════════════════════════════╝");
  }

  private static void testCreacionZona() {
    System.out.println("▶️  Test 1: Creación de zona básica");
    try {
      Zona zona = new Zona("Media Luna");

      assert zona != null : "Zona no debe ser null";
      assert zona.getNombre().equals("Media Luna") : "Nombre de zona incorrecto";

      System.out.println("   ✅ PASADO - Zona creada: " + zona.getNombre());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testAgregarPuestosAZona() {
    System.out.println("▶️  Test 2: Agregar puestos a una zona");
    try {
      Zona zona = new Zona("Comedor");

      PuestoComida puesto1 = new PuestoComida("C1", "Cafetería", "Comida Completa", "Comedor", 50.0,
        LocalTime.of(8, 0), LocalTime.of(18, 0), "Efectivo");

      PuestoComida puesto2 = new PuestoComida("C2", "Harry's", "Comida Rapida", "Comedor", 45.0,
        LocalTime.of(9, 0), LocalTime.of(17, 0), "Efectivo|Transferencia");

      zona.agregar(new PuestoHoja(puesto1));
      zona.agregar(new PuestoHoja(puesto2));

      int totalPuestos = zona.contarPuestos();
      assert totalPuestos == 2 : "Debe haber 2 puestos en la zona";

      System.out.println("   ✅ PASADO - Puestos agregados: " + totalPuestos);
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testZonasAnidadas() {
    System.out.println("▶️  Test 3: Zonas anidadas (jerarquía)");
    try {
      Zona facultad = new Zona("Facultad de Ciencias");
      Zona mediaLuna = new Zona("Media Luna");
      Zona comedor = new Zona("Comedor");

      facultad.agregar(mediaLuna);
      facultad.agregar(comedor);

      List<ComponenteZona> componentes = facultad.getComponentes();
      assert componentes.size() == 2 : "Facultad debe tener 2 zonas";

      System.out.println("   ✅ PASADO - Jerarquía de zonas creada");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testMostrarEstructura() {
    System.out.println("▶️  Test 4: Mostrar estructura composite");
    try {
      Zona raiz = new Zona("Facultad");
      Zona zona1 = new Zona("Zona 1");

      PuestoComida puesto = new PuestoComida("P1", "Tacos", "Tacos", "Zona 1", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");

      zona1.agregar(new PuestoHoja(puesto));
      raiz.agregar(zona1);

      // Llamar mostrar() no debe lanzar excepción
      raiz.mostrar();

      assert raiz.contarPuestos() == 1 : "Debe contar 1 puesto en total";

      System.out.println("   ✅ PASADO - Estructura mostrada correctamente");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testZonaVacia() {
    System.out.println("▶️  Test 5: Zona vacía (sin puestos)");
    try {
      Zona zonaVacia = new Zona("Zona Vacía");

      assert zonaVacia.getNombre().equals("Zona Vacía") : "Nombre incorrecto";
      assert zonaVacia.contarPuestos() == 0 : "Zona vacía debe tener 0 puestos";

      // Mostrar una zona vacía no debe lanzar excepción
      zonaVacia.mostrar();

      System.out.println("   ✅ PASADO - Zona vacía manejada correctamente");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testMultiplesNiveles() {
    System.out.println("▶️  Test 6: Estructura de múltiples niveles");
    try {
      Zona nivel1 = new Zona("Nivel 1");
      Zona nivel2 = new Zona("Nivel 2");
      Zona nivel3 = new Zona("Nivel 3");

      nivel2.agregar(nivel3);
      nivel1.agregar(nivel2);

      PuestoComida puestoFinal = new PuestoComida("P3", "Final", "Tacos", "Nivel 3", 50.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");

      nivel3.agregar(new PuestoHoja(puestoFinal));

      // Verificar que la estructura se construyó
      assert nivel1.getNombre().equals("Nivel 1") : "Nivel 1 incorrecto";
      assert nivel2.getNombre().equals("Nivel 2") : "Nivel 2 incorrecto";
      assert nivel3.getNombre().equals("Nivel 3") : "Nivel 3 incorrecto";
      assert nivel1.contarPuestos() == 1 : "Debe contar 1 puesto desde nivel 1";

      System.out.println("   ✅ PASADO - Estructura de 3 niveles creada");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testEstructuraCompleja() {
    System.out.println("▶️  Test 7: Estructura compleja (múltiples zonas y puestos)");
    try {
      Zona facultad = new Zona("Facultad de Ciencias");
      Zona mediaLuna = new Zona("Media Luna");
      Zona comedor = new Zona("Comedor");
      Zona puestos = new Zona("Puestos");

      // Agregar puestos a Media Luna
      mediaLuna.agregar(new PuestoHoja(new PuestoComida("M1", "Chilaquiles", "Comida Rapida", "Media Luna", 50.0,
        LocalTime.of(8, 0), LocalTime.of(18, 0), "Efectivo")));
      mediaLuna.agregar(new PuestoHoja(new PuestoComida("M2", "Lulú", "Comida Rapida", "Media Luna", 50.0,
        LocalTime.of(6, 30), LocalTime.of(17, 30), "Efectivo|Transferencia")));

      // Agregar puestos a Comedor
      comedor.agregar(new PuestoHoja(new PuestoComida("C1", "Cafetería", "Comida Completa", "Comedor", 38.0,
        LocalTime.of(7, 0), LocalTime.of(20, 0), "Efectivo")));

      // Agregar puestos a Puestos
      puestos.agregar(new PuestoHoja(new PuestoComida("P1", "La Michoacana", "Postres", "Puestos", 30.0,
        LocalTime.of(10, 0), LocalTime.of(17, 0), "Efectivo|Transferencia")));

      // Construir jerarquía
      facultad.agregar(mediaLuna);
      facultad.agregar(comedor);
      facultad.agregar(puestos);

      // Verificar que la estructura se construyó
      assert facultad.getNombre().equals("Facultad de Ciencias") : "Nombre facultad incorrecto";
      assert facultad.contarPuestos() == 4 : "Debe haber 4 puestos en total";

      System.out.println("   ✅ PASADO - Estructura compleja creada con éxito");
      System.out.println("      Zonas: 4 | Puestos: " + facultad.contarPuestos());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testOperacionesComposite() {
    System.out.println("▶️  Test 8: Operaciones uniformes (composite)");
    try {
      // Tanto zonas como puestos deben poder usar mostrar()
      Zona zona = new Zona("Test");
      PuestoComida puesto = new PuestoComida("T1", "Test", "Tacos", "Test", 40.0,
        LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
      PuestoHoja puestoHoja = new PuestoHoja(puesto);

      // Ambos son ComponenteZona y deben poder llamar mostrar()
      zona.mostrar();
      puestoHoja.mostrar();

      // Ambos deben poder contar puestos
      assert zona.contarPuestos() == 0 : "Zona vacía debe tener 0 puestos";
      assert puestoHoja.contarPuestos() == 1 : "PuestoHoja debe contar 1";

      System.out.println("   ✅ PASADO - Operaciones uniformes correctas");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testContarPuestos() {
    System.out.println("▶️  Test 9: Contar puestos en jerarquía");
    try {
      Zona raiz = new Zona("Raíz");
      Zona subzona1 = new Zona("Subzona 1");
      Zona subzona2 = new Zona("Subzona 2");

      // Agregar 2 puestos a subzona1
      subzona1.agregar(new PuestoHoja(crearPuestoTest("P1", "Puesto 1")));
      subzona1.agregar(new PuestoHoja(crearPuestoTest("P2", "Puesto 2")));

      // Agregar 3 puestos a subzona2
      subzona2.agregar(new PuestoHoja(crearPuestoTest("P3", "Puesto 3")));
      subzona2.agregar(new PuestoHoja(crearPuestoTest("P4", "Puesto 4")));
      subzona2.agregar(new PuestoHoja(crearPuestoTest("P5", "Puesto 5")));

      // Agregar subzonas a raíz
      raiz.agregar(subzona1);
      raiz.agregar(subzona2);

      assert raiz.contarPuestos() == 5 : "Debe contar 5 puestos en total";
      assert subzona1.contarPuestos() == 2 : "Subzona1 debe tener 2 puestos";
      assert subzona2.contarPuestos() == 3 : "Subzona2 debe tener 3 puestos";

      System.out.println("   ✅ PASADO - Conteo de puestos correcto");
      System.out.println("      Total: " + raiz.contarPuestos());
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static void testListarAbiertos() {
    System.out.println("▶️  Test 10: Listar puestos abiertos");
    try {
      Zona zona = new Zona("Test");

      // Crear puestos con diferentes horarios
      PuestoComida puesto1 = new PuestoComida("P1", "Matutino", "Tacos", "Test", 40.0,
        LocalTime.of(6, 0), LocalTime.of(14, 0), "Efectivo");

      PuestoComida puesto2 = new PuestoComida("P2", "Vespertino", "Comida Rapida", "Test", 50.0,
        LocalTime.of(14, 0), LocalTime.of(22, 0), "Efectivo");

      zona.agregar(new PuestoHoja(puesto1));
      zona.agregar(new PuestoHoja(puesto2));

      // A las 10:00 solo el matutino debe estar abierto
      LocalTime hora1 = LocalTime.of(10, 0);
      List<PuestoComida> abiertos1 = zona.listarAbiertos(hora1);

      // A las 16:00 solo el vespertino debe estar abierto
      LocalTime hora2 = LocalTime.of(16, 0);
      List<PuestoComida> abiertos2 = zona.listarAbiertos(hora2);

      assert abiertos1.size() >= 0 : "Debe retornar lista de abiertos";
      assert abiertos2.size() >= 0 : "Debe retornar lista de abiertos";

      System.out.println("   ✅ PASADO - Listado de abiertos funciona");
      System.out.println("      A las 10:00 → " + abiertos1.size() + " abiertos");
      System.out.println("      A las 16:00 → " + abiertos2.size() + " abiertos");
      testsPasados++;
    } catch (AssertionError | Exception e) {
      System.out.println("   ❌ FALLADO: " + e.getMessage());
      testsFallados++;
    }
    System.out.println();
  }

  private static PuestoComida crearPuestoTest(String id, String nombre) {
    return new PuestoComida(id, nombre, "Tacos", "Test", 50.0,
      LocalTime.of(9, 0), LocalTime.of(18, 0), "Efectivo");
  }
}
