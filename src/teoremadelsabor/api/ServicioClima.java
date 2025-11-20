package teoremadelsabor.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servicio que consume una API externa de clima.
 *
 * <p>Características implementadas:</p>
 * <ul>
 *   <li>Consumo de API REST externa (wttr.in)</li>
 *   <li>Parseo manual de respuesta JSON usando regex</li>
 *   <li>Sistema de caché con expiración temporal (15 minutos)</li>
 *   <li>Manejo robusto de errores de red y timeouts</li>
 *   <li>Logging detallado para debugging</li>
 *   <li>Fallback a datos en caché si hay errores</li>
 *   <li>Información extendida: temperatura, humedad, sensación térmica, viento</li>
 *   <li>Recomendaciones personalizadas según condiciones climáticas</li>
 * </ul>
 *
 * <p>API utilizada: wttr.in (gratuita, sin necesidad de API key)</p>
 *
 * @author Teorema del Sabor Team
 * @version 2.0
 * @see <a href="https://wttr.in/:help">Documentación wttr.in</a>
 */
public class ServicioClima {

    private static final Logger LOGGER = Logger.getLogger(ServicioClima.class.getName());

    /**
     * Clase interna que encapsula toda la información del clima.
     * Incluye datos actuales y recomendaciones personalizadas.
     */
    public static class ClimaInfo {
        public final String descripcion;
        public final String temperatura;
        public final String sensacionTermica;
        public final String humedad;
        public final String viento;
        public final String precipitacion;
        public final String recomendacion;
        public final LocalDateTime horaConsulta;
        public final boolean fromCache;

        /**
         * Constructor completo de ClimaInfo.
         *
         * @param descripcion Descripción textual del clima (ej: "Partly cloudy")
         * @param temperatura Temperatura actual en °C
         * @param sensacionTermica Sensación térmica en °C
         * @param humedad Porcentaje de humedad
         * @param viento Velocidad del viento en km/h
         * @param precipitacion Probabilidad de precipitación en mm
         * @param recomendacion Recomendación personalizada según el clima
         * @param fromCache Indica si los datos provienen del caché
         */
        ClimaInfo(String descripcion, String temperatura, String sensacionTermica,
                  String humedad, String viento, String precipitacion,
                  String recomendacion, boolean fromCache) {
            this.descripcion = descripcion;
            this.temperatura = temperatura;
            this.sensacionTermica = sensacionTermica;
            this.humedad = humedad;
            this.viento = viento;
            this.precipitacion = precipitacion;
            this.recomendacion = recomendacion;
            this.horaConsulta = LocalDateTime.now();
            this.fromCache = fromCache;
        }

        /**
         * Representación en texto del clima para mostrar al usuario.
         */
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String cache = fromCache ? "[CACHÉ]" : "[EN VIVO]";
            String recomCorta = recomendacion.length() > 48
                ? recomendacion.substring(0, 45) + "..."
                : recomendacion;

            return String.format(
                "╔══════════════════════════════════════════════════════════╗\n" +
                "║          🌤️  CLIMA EN CIUDAD DE MÉXICO 🌤️               ║\n" +
                "╠══════════════════════════════════════════════════════════╣\n" +
                "║ Condición:     %-42s║\n" +
                "║ Temperatura:   %-42s║\n" +
                "║ Sensación:     %-42s║\n" +
                "║ Humedad:       %-42s║\n" +
                "║ Viento:        %-42s║\n" +
                "║ Precipitación: %-42s║\n" +
                "╠══════════════════════════════════════════════════════════╣\n" +
                "║ 💡 %-54s║\n" +
                "╠══════════════════════════════════════════════════════════╣\n" +
                "║ Actualizado: %-25s %18s║\n" +
                "╚══════════════════════════════════════════════════════════╝",
                descripcion,
                temperatura,
                sensacionTermica,
                humedad,
                viento,
                precipitacion,
                recomCorta,
                horaConsulta.format(formatter),
                cache
            );
        }

        /**
         * Versión simplificada para GUI (sin caracteres especiales).
         */
        public String toSimpleString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String cache = fromCache ? "Cache" : "En vivo";

            return String.format(
                "\u2601\uFE0F %s - %s | %s | %s (%s)",
                descripcion,
                temperatura,
                humedad,
                cache,
                horaConsulta.format(formatter)
            );
        }

        /**
         * Retorna una versión compacta de la información climática.
         */
        public String toStringCompact() {
            return String.format("Clima CDMX: %s, %s. %s", descripcion, temperatura, recomendacion);
        }
    }

    // Configuración de la API
    private static final String URL_API = "https://wttr.in/Mexico_City?format=j1";
    private static final long CACHE_DURATION_MINUTES = 15;
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;

    // Variables de caché
    private static ClimaInfo cachedClima = null;
    private static LocalDateTime lastFetchTime = null;

    // Estadísticas
    private static int totalRequests = 0;
    private static int cacheHits = 0;
    private static int apiCalls = 0;
    private static int errors = 0;

    /**
     * Obtiene la información del clima, usando caché si es posible.
     *
     * <p>Este método implementa las siguientes estrategias:</p>
     * <ul>
     *   <li>Si el caché es válido (menos de 15 minutos), retorna datos en caché</li>
     *   <li>Si el caché expiró, consulta la API externa</li>
     *   <li>Si la API falla, retorna el caché antiguo si existe</li>
     *   <li>Si no hay caché y la API falla, retorna datos por defecto</li>
     * </ul>
     *
     * @return Un objeto ClimaInfo con los datos del clima y recomendaciones
     */
    public static ClimaInfo obtenerClimaInfo() {
        totalRequests++;

        // Verificar si el caché es válido
        if (cachedClima != null && lastFetchTime != null &&
            Duration.between(lastFetchTime, LocalDateTime.now()).toMinutes() < CACHE_DURATION_MINUTES) {
            cacheHits++;
            LOGGER.log(Level.INFO, "Retornando datos del caché. Cache hits: {0}/{1}",
                new Object[]{cacheHits, totalRequests});
            return new ClimaInfo(
                cachedClima.descripcion,
                cachedClima.temperatura,
                cachedClima.sensacionTermica,
                cachedClima.humedad,
                cachedClima.viento,
                cachedClima.precipitacion,
                cachedClima.recomendacion,
                true
            );
        }

        // Intentar obtener datos frescos de la API
        LOGGER.log(Level.INFO, "Consultando API de clima...");
        apiCalls++;

        HttpURLConnection conn = null;
        try {
            URI uri = new URI(URL_API);
            conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setRequestProperty("User-Agent", "TeoremaDelSabor/2.0");

            int responseCode = conn.getResponseCode();
            LOGGER.log(Level.INFO, "Código de respuesta HTTP: {0}", responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                String json = response.toString();
                LOGGER.log(Level.FINE, "Respuesta JSON recibida (primeros 100 chars): {0}",
                    json.substring(0, Math.min(100, json.length())));

                // Parsear todos los campos del JSON desde current_condition
                String descripcion = parseJson(json, "\"weatherDesc\"\\s*:\\s*\\[\\s*\\{\\s*\"value\"\\s*:\\s*\"([^\"]+)\"");
                String temperatura = parseJson(json, "\"temp_C\"\\s*:\\s*\"([^\"]+)\"");
                String sensacionTermica = parseJson(json, "\"FeelsLikeC\"\\s*:\\s*\"([^\"]+)\"");
                String humedad = parseJson(json, "\"humidity\"\\s*:\\s*\"([^\"]+)\"");
                String viento = parseJson(json, "\"windspeedKmph\"\\s*:\\s*\"([^\"]+)\"");
                String precipitacion = parseJson(json, "\"precipMM\"\\s*:\\s*\"([^\"]+)\"");

                if (descripcion != null && temperatura != null) {
                    String recomendacion = generarRecomendacion(descripcion, temperatura, humedad);

                    cachedClima = new ClimaInfo(
                        descripcion,
                        temperatura + "°C",
                        (sensacionTermica != null ? sensacionTermica + "°C" : "N/A"),
                        (humedad != null ? humedad + "%" : "N/A"),
                        (viento != null ? viento + " km/h" : "N/A"),
                        (precipitacion != null ? precipitacion + " mm" : "N/A"),
                        recomendacion,
                        false
                    );

                    lastFetchTime = LocalDateTime.now();
                    LOGGER.log(Level.INFO, "Datos del clima actualizados exitosamente");
                    return cachedClima;
                } else {
                    LOGGER.log(Level.WARNING, "No se pudieron parsear los campos requeridos del JSON");
                }
            } else {
                LOGGER.log(Level.WARNING, "API retornó código HTTP {0}", responseCode);
            }
        } catch (URISyntaxException e) {
            errors++;
            LOGGER.log(Level.SEVERE, "Error en la URI: {0}", e.getMessage());
        } catch (IOException e) {
            errors++;
            LOGGER.log(Level.SEVERE, "Error de I/O al consultar API: {0}", e.getMessage());
        } catch (Exception e) {
            errors++;
            LOGGER.log(Level.SEVERE, "Error inesperado: {0}", e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Fallback: Si hay caché antiguo, usarlo
        if (cachedClima != null) {
            LOGGER.log(Level.WARNING, "Usando caché antiguo como fallback");
            return new ClimaInfo(
                cachedClima.descripcion,
                cachedClima.temperatura,
                cachedClima.sensacionTermica,
                cachedClima.humedad,
                cachedClima.viento,
                cachedClima.precipitacion,
                cachedClima.recomendacion + " (Datos desactualizados)",
                true
            );
        }

        // Último recurso: datos por defecto
        LOGGER.log(Level.SEVERE, "No hay datos disponibles. Retornando valores por defecto.");
        return new ClimaInfo(
            "No disponible",
            "--°C",
            "--°C",
            "--%",
            "-- km/h",
            "-- mm",
            "Verifica tu conexión a internet para obtener el clima actual.",
            false
        );
    }

    /**
     * Genera recomendaciones personalizadas basadas en las condiciones climáticas.
     *
     * <p>Analiza la descripción del clima, temperatura y humedad para sugerir
     * las mejores zonas del Teorema del Sabor según las condiciones actuales.</p>
     *
     * @param descripcionClima Descripción textual del clima
     * @param temperatura Temperatura actual en °C (como String)
     * @param humedad Humedad actual en % (como String)
     * @return Recomendación personalizada para el usuario
     */
    private static String generarRecomendacion(String descripcionClima, String temperatura, String humedad) {
        String climaLower = descripcionClima.toLowerCase();
        int temp = 0;
        int hum = 0;

        // Parsear temperatura y humedad de forma segura
        try {
            if (temperatura != null && !temperatura.isEmpty()) {
                temp = Integer.parseInt(temperatura);
            }
            if (humedad != null && !humedad.isEmpty()) {
                hum = Integer.parseInt(humedad);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error parseando temperatura/humedad: {0}", e.getMessage());
        }

        // Recomendaciones por condiciones climáticas
        if (climaLower.contains("rain") || climaLower.contains("shower") ||
            climaLower.contains("lluvia") || climaLower.contains("drizzle")) {
            return "🌧️ Lluvia detectada - Visita puestos techados en Media Luna";
        }

        if (climaLower.contains("storm") || climaLower.contains("thunder")) {
            return "⛈️ Tormenta - Busca refugio en zonas cubiertas (Media Luna)";
        }

        // Recomendaciones por temperatura
        if (temp > 28) {
            return "☀️ Día caluroso (" + temp + "°C) - Prueba bebidas frías y helados";
        }

        if (temp < 15) {
            return "🥶 Día frío (" + temp + "°C) - Perfecto para pozole o atole caliente";
        }

        // Recomendaciones por clima despejado
        if (climaLower.contains("clear") || climaLower.contains("sunny")) {
            if (temp >= 20 && temp <= 28) {
                return "🌤️ Clima perfecto - Disfruta al aire libre en Estacionamiento";
            }
            return "☀️ Día soleado - Ideal para explorar todas las zonas";
        }

        // Recomendaciones por nubosidad
        if (climaLower.contains("cloudy") || climaLower.contains("overcast")) {
            return "☁️ Clima agradable - Buena temperatura para recorrer todos los puestos";
        }

        // Recomendaciones por humedad
        if (hum > 80) {
            return "💧 Alta humedad (" + hum + "%) - Mantente hidratado, busca bebidas refrescantes";
        }

        // Recomendación genérica
        return "🍴 Clima estable - Explora los " + (temp > 20 ? "antojitos" : "platillos calientes");
    }

    /**
     * Obtiene estadísticas de uso del servicio de clima.
     *
     * @return String con las estadísticas formateadas
     */
    public static String obtenerEstadisticas() {
        double cacheEfficiency = totalRequests > 0
            ? (cacheHits * 100.0 / totalRequests)
            : 0.0;

        return String.format(
            "\n╔══════════════════════════════════════════╗\n" +
            "║     📊 ESTADÍSTICAS API CLIMA 📊        ║\n" +
            "╠══════════════════════════════════════════╣\n" +
            "║ Total de solicitudes:     %-14d║\n" +
            "║ Hits de caché:            %-14d║\n" +
            "║ Llamadas a API:           %-14d║\n" +
            "║ Errores:                  %-14d║\n" +
            "║ Eficiencia de caché:      %.1f%%%-11s║\n" +
            "╚══════════════════════════════════════════╝",
            totalRequests,
            cacheHits,
            apiCalls,
            errors,
            cacheEfficiency,
            ""
        );
    }

    /**
     * Limpia el caché forzando una nueva consulta en la siguiente petición.
     */
    public static void limpiarCache() {
        cachedClima = null;
        lastFetchTime = null;
        LOGGER.log(Level.INFO, "Caché limpiado manualmente");
    }

    /**
     * Parsea un campo del JSON usando expresiones regulares.
     *
     * @param json String con el JSON completo
     * @param regex Expresión regular para extraer el campo
     * @return El valor extraído o null si no se encuentra
     */
    private static String parseJson(String json, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error parseando JSON con regex: {0}", e.getMessage());
        }
        return null;
    }
}

