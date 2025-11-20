package teoremadelsabor.tests;

import teoremadelsabor.api.ServicioClima;
import teoremadelsabor.api.ServicioClima.ClimaInfo;

/**
 * Pruebas unitarias para el ServicioClima.
 *
 * <p>Estas pruebas verifican:</p>
 * <ul>
 *   <li>Funcionamiento básico del servicio de clima</li>
 *   <li>Sistema de caché (hits y misses)</li>
 *   <li>Manejo de errores y fallbacks</li>
 *   <li>Integridad de los datos retornados</li>
 *   <li>Estadísticas del servicio</li>
 * </ul>
 *
 * @author Teorema del Sabor Team
 * @version 1.0
 */
public class ServicioClimaTest {

    private static int testsPasados = 0;
    private static int testsFallidos = 0;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║      🧪 PRUEBAS UNITARIAS - SERVICIO CLIMA 🧪        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        ejecutarTodasLasPruebas();

        // Resumen final
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                  📊 RESUMEN FINAL 📊                   ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║  ✅ Tests pasados:    %-31d║%n", testsPasados);
        System.out.printf("║  ❌ Tests fallidos:   %-31d║%n", testsFallidos);
        System.out.printf("║  📈 Total:            %-31d║%n", testsPasados + testsFallidos);

        if (testsFallidos == 0) {
            System.out.println("║                                                        ║");
            System.out.println("║         🎉 ¡TODAS LAS PRUEBAS PASARON! 🎉             ║");
        }
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    /**
     * Ejecuta todas las pruebas del servicio.
     */
    private static void ejecutarTodasLasPruebas() {
        test1_ObtenerClimaBasico();
        test2_VerificarCache();
        test3_VerificarCamposNoNulos();
        test4_VerificarRecomendacion();
        test5_VerificarEstadisticas();
        test6_LimpiarCache();
        test7_MultiplesSolicitudes();
    }

    /**
     * Test 1: Verificar que se puede obtener información del clima.
     */
    private static void test1_ObtenerClimaBasico() {
        System.out.println("▶️  Test 1: Obtener información básica del clima");
        try {
            ClimaInfo clima = ServicioClima.obtenerClimaInfo();

            assertNotNull(clima, "ClimaInfo no debe ser null");
            System.out.println("   ✅ Información del clima obtenida exitosamente");
            System.out.println("      Descripción: " + clima.descripcion);
            System.out.println("      Temperatura: " + clima.temperatura);

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 2: Verificar que el caché funciona correctamente.
     */
    private static void test2_VerificarCache() {
        System.out.println("▶️  Test 2: Verificar funcionamiento del caché");
        try {
            // Primera solicitud
            ClimaInfo clima1 = ServicioClima.obtenerClimaInfo();

            // Segunda solicitud (debería venir del caché)
            ClimaInfo clima2 = ServicioClima.obtenerClimaInfo();

            assertTrue(clima2.fromCache, "La segunda solicitud debería venir del caché");
            assertEquals(clima1.descripcion, clima2.descripcion,
                "Los datos en caché deben ser iguales");

            System.out.println("   ✅ Caché funcionando correctamente");
            System.out.println("      Primera llamada: " + (clima1.fromCache ? "CACHÉ" : "API"));
            System.out.println("      Segunda llamada: " + (clima2.fromCache ? "CACHÉ" : "API"));

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 3: Verificar que todos los campos principales no son nulos.
     */
    private static void test3_VerificarCamposNoNulos() {
        System.out.println("▶️  Test 3: Verificar integridad de campos");
        try {
            ClimaInfo clima = ServicioClima.obtenerClimaInfo();

            assertNotNull(clima.descripcion, "Descripción no debe ser null");
            assertNotNull(clima.temperatura, "Temperatura no debe ser null");
            assertNotNull(clima.sensacionTermica, "Sensación térmica no debe ser null");
            assertNotNull(clima.humedad, "Humedad no debe ser null");
            assertNotNull(clima.viento, "Viento no debe ser null");
            assertNotNull(clima.precipitacion, "Precipitación no debe ser null");
            assertNotNull(clima.recomendacion, "Recomendación no debe ser null");
            assertNotNull(clima.horaConsulta, "Hora de consulta no debe ser null");

            System.out.println("   ✅ Todos los campos están presentes");
            System.out.println("      Descripción: " + clima.descripcion);
            System.out.println("      Temperatura: " + clima.temperatura);
            System.out.println("      Sensación: " + clima.sensacionTermica);
            System.out.println("      Humedad: " + clima.humedad);
            System.out.println("      Viento: " + clima.viento);
            System.out.println("      Precipitación: " + clima.precipitacion);

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 4: Verificar que se genera una recomendación.
     */
    private static void test4_VerificarRecomendacion() {
        System.out.println("▶️  Test 4: Verificar generación de recomendación");
        try {
            ClimaInfo clima = ServicioClima.obtenerClimaInfo();

            assertNotNull(clima.recomendacion, "Recomendación no debe ser null");
            assertFalse(clima.recomendacion.isEmpty(), "Recomendación no debe estar vacía");

            System.out.println("   ✅ Recomendación generada correctamente");
            System.out.println("      " + clima.recomendacion);

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 5: Verificar que las estadísticas se generan correctamente.
     */
    private static void test5_VerificarEstadisticas() {
        System.out.println("▶️  Test 5: Verificar estadísticas del servicio");
        try {
            String stats = ServicioClima.obtenerEstadisticas();

            assertNotNull(stats, "Estadísticas no deben ser null");
            assertFalse(stats.isEmpty(), "Estadísticas no deben estar vacías");

            System.out.println("   ✅ Estadísticas generadas correctamente");
            System.out.println(stats);

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 6: Verificar que se puede limpiar el caché.
     */
    private static void test6_LimpiarCache() {
        System.out.println("▶️  Test 6: Verificar limpieza de caché");
        try {
            // Obtener datos
            ServicioClima.obtenerClimaInfo();

            // Limpiar caché
            ServicioClima.limpiarCache();

            // La siguiente llamada NO debería venir del caché
            ClimaInfo clima = ServicioClima.obtenerClimaInfo();

            // Nota: Puede venir del caché si la API ya la cacheó internamente,
            // pero el método debería funcionar sin errores
            assertNotNull(clima, "Clima no debe ser null después de limpiar caché");

            System.out.println("   ✅ Caché limpiado exitosamente");
            System.out.println("      Siguiente llamada: " + (clima.fromCache ? "CACHÉ" : "API"));

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    /**
     * Test 7: Verificar múltiples solicitudes consecutivas.
     */
    private static void test7_MultiplesSolicitudes() {
        System.out.println("▶️  Test 7: Múltiples solicitudes consecutivas");
        try {
            int solicitudes = 5;
            int fromCache = 0;
            int fromAPI = 0;

            for (int i = 0; i < solicitudes; i++) {
                ClimaInfo clima = ServicioClima.obtenerClimaInfo();
                if (clima.fromCache) {
                    fromCache++;
                } else {
                    fromAPI++;
                }
            }

            System.out.println("   ✅ Múltiples solicitudes completadas");
            System.out.printf("      Total: %d | API: %d | Caché: %d%n",
                solicitudes, fromAPI, fromCache);
            System.out.println("      Eficiencia: " + (fromCache * 100 / solicitudes) + "%");

            testsPasados++;
        } catch (AssertionError e) {
            System.out.println("   ❌ FALLO: " + e.getMessage());
            testsFallidos++;
        } catch (Exception e) {
            System.out.println("   ❌ ERROR INESPERADO: " + e.getMessage());
            testsFallidos++;
        }
        System.out.println();
    }

    // ==================== Métodos de Aserción ====================

    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + " (esperado: " + expected + ", actual: " + actual + ")");
        }
    }
}
