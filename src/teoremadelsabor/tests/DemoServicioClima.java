package teoremadelsabor.tests;

import teoremadelsabor.api.ServicioClima;
import teoremadelsabor.api.ServicioClima.ClimaInfo;

/**
 * Demostración completa del ServicioClima.
 *
 * <p>Este programa demuestra todas las características de la integración con API:</p>
 * <ul>
 *   <li>Consumo de API REST externa (wttr.in)</li>
 *   <li>Parseo de respuesta JSON</li>
 *   <li>Sistema de caché inteligente</li>
 *   <li>Manejo de errores y fallbacks</li>
 *   <li>Logging detallado</li>
 *   <li>Recomendaciones personalizadas</li>
 *   <li>Estadísticas de uso</li>
 * </ul>
 *
 * @author Teorema del Sabor Team
 * @version 1.0
 */
public class DemoServicioClima {

    public static void main(String[] args) {
        mostrarBanner();

        System.out.println("\n📡 DEMOSTRACIÓN: Integración con API Externa de Clima\n");
        System.out.println("━".repeat(60));

        // Demo 1: Primera consulta (llamada a API)
        System.out.println("\n1️⃣  Primera consulta (debe llamar a la API):");
        System.out.println("─".repeat(60));
        esperarUnMomento(500);
        ClimaInfo clima1 = ServicioClima.obtenerClimaInfo();
        System.out.println(clima1);

        // Demo 2: Segunda consulta (debe venir del caché)
        System.out.println("\n2️⃣  Segunda consulta inmediata (debe usar caché):");
        System.out.println("─".repeat(60));
        esperarUnMomento(500);
        ClimaInfo clima2 = ServicioClima.obtenerClimaInfo();
        System.out.println(clima2.toStringCompact());
        System.out.println("\n   📌 Nota: Fuente = " + (clima2.fromCache ? "CACHÉ ✅" : "API"));

        // Demo 3: Mostrar estadísticas
        System.out.println("\n3️⃣  Estadísticas del servicio:");
        System.out.println("─".repeat(60));
        System.out.println(ServicioClima.obtenerEstadisticas());

        // Demo 4: Múltiples consultas rápidas
        System.out.println("\n4️⃣  Test de rendimiento: 10 consultas rápidas:");
        System.out.println("─".repeat(60));
        long inicio = System.currentTimeMillis();
        for (int i = 1; i <= 10; i++) {
            ServicioClima.obtenerClimaInfo();
            System.out.print(".");
        }
        long fin = System.currentTimeMillis();
        System.out.println("\n   ⏱️  Tiempo total: " + (fin - inicio) + "ms");
        System.out.println("   ⚡ Promedio por consulta: " + ((fin - inicio) / 10) + "ms");

        // Demo 5: Estadísticas actualizadas
        System.out.println("\n5️⃣  Estadísticas actualizadas:");
        System.out.println("─".repeat(60));
        System.out.println(ServicioClima.obtenerEstadisticas());

        // Demo 6: Limpiar caché y nueva consulta
        System.out.println("\n6️⃣  Limpiar caché y forzar nueva consulta:");
        System.out.println("─".repeat(60));
        ServicioClima.limpiarCache();
        System.out.println("   🧹 Caché limpiado");
        esperarUnMomento(500);
        ClimaInfo clima3 = ServicioClima.obtenerClimaInfo();
        System.out.println("\n   📌 Nueva consulta: " + (clima3.fromCache ? "CACHÉ" : "API ✅"));

        // Demo 7: Información detallada del clima
        System.out.println("\n7️⃣  Información detallada del clima actual:");
        System.out.println("─".repeat(60));
        mostrarInfoDetallada(clima3);

        // Demo 8: Estadísticas finales
        System.out.println("\n8️⃣  Estadísticas finales:");
        System.out.println("─".repeat(60));
        System.out.println(ServicioClima.obtenerEstadisticas());

        // Resumen de características
        mostrarResumenCaracteristicas();

        System.out.println("\n━".repeat(60));
        System.out.println("✅ Demostración completada exitosamente");
        System.out.println("━".repeat(60) + "\n");
    }

    private static void mostrarBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║         🌐 PUNTO EXTRA: INTEGRACIÓN CON API 🌐            ║");
        System.out.println("║                                                            ║");
        System.out.println("║              Demostración de ServicioClima                 ║");
        System.out.println("║                 Teorema del Sabor v2.0                     ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    private static void mostrarInfoDetallada(ClimaInfo clima) {
        System.out.println("\n   🌡️  Temperatura:        " + clima.temperatura);
        System.out.println("   🤔 Sensación térmica:   " + clima.sensacionTermica);
        System.out.println("   💧 Humedad:             " + clima.humedad);
        System.out.println("   💨 Viento:              " + clima.viento);
        System.out.println("   🌧️  Precipitación:      " + clima.precipitacion);
        System.out.println("   ☁️  Condición:          " + clima.descripcion);
        System.out.println("   💡 Recomendación:       " + clima.recomendacion);
        System.out.println("   🕐 Hora consulta:       " + clima.horaConsulta);
    }

    private static void mostrarResumenCaracteristicas() {
        System.out.println("\n\n📋 CARACTERÍSTICAS IMPLEMENTADAS:");
        System.out.println("━".repeat(60));
        System.out.println("✅ Consumo de API REST externa (wttr.in)");
        System.out.println("✅ Parseo manual de JSON usando expresiones regulares");
        System.out.println("✅ Sistema de caché con expiración (15 minutos)");
        System.out.println("✅ Manejo robusto de errores de red");
        System.out.println("✅ Timeouts configurables (5 segundos)");
        System.out.println("✅ Logging detallado con java.util.logging");
        System.out.println("✅ Fallback a caché antiguo si falla la API");
        System.out.println("✅ Información completa: temperatura, humedad, viento, etc.");
        System.out.println("✅ Recomendaciones personalizadas según clima");
        System.out.println("✅ Estadísticas de uso y eficiencia de caché");
        System.out.println("✅ Método para limpiar caché manualmente");
        System.out.println("✅ Documentación Javadoc completa");
        System.out.println("✅ Pruebas unitarias exhaustivas (7 tests)");
        System.out.println("✅ Formateo elegante de salida");
        System.out.println("━".repeat(60));
    }

    private static void esperarUnMomento(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
