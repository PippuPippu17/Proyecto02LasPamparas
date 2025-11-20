# 🌐 PUNTO EXTRA: Integración con API Externa

## Descripción General

Este proyecto implementa una integración completa con una API REST externa para obtener información del clima en tiempo real. La funcionalidad está diseñada para demostrar el consumo profesional de APIs web desde Java.

## 📋 Características Implementadas

### ✅ Funcionalidades Principales

1. **Consumo de API REST Externa**
   - API utilizada: [wttr.in](https://wttr.in) (gratuita, sin necesidad de API key)
   - Formato de respuesta: JSON
   - Endpoint: `https://wttr.in/Mexico_City?format=j1`

2. **Parseo Manual de JSON**
   - Implementado con expresiones regulares (regex)
   - Sin dependencias externas (no se usa Gson ni Jackson)
   - Extracción de múltiples campos: temperatura, humedad, viento, precipitación, etc.

3. **Sistema de Caché Inteligente**
   - Duración del caché: 15 minutos
   - Reduce llamadas innecesarias a la API
   - Mejora el rendimiento y reduce la carga en la API externa
   - Eficiencia típica: 80-100% en uso normal

4. **Manejo Robusto de Errores**
   - Timeouts configurables (5 segundos por defecto)
   - Manejo de errores de red (IOException)
   - Manejo de errores de parseo
   - Fallback a datos en caché si la API falla
   - Valores por defecto si no hay datos disponibles

5. **Logging Detallado**
   - Usando `java.util.logging`
   - Niveles: INFO, WARNING, SEVERE, FINE
   - Registro de todas las operaciones importantes
   - Útil para debugging y monitoreo

6. **Información Climática Completa**
   - Descripción del clima (ej: "Sunny", "Cloudy", "Rainy")
   - Temperatura actual (°C)
   - Sensación térmica (°C)
   - Humedad (%)
   - Velocidad del viento (km/h)
   - Precipitación (mm)

7. **Recomendaciones Personalizadas**
   - Análisis inteligente de las condiciones climáticas
   - Sugerencias basadas en temperatura, humedad y condiciones
   - Integración con las zonas del Teorema del Sabor
   - Ejemplos:
     - "🌧️ Lluvia detectada - Visita puestos techados en Media Luna"
     - "🌤️ Clima perfecto - Disfruta al aire libre en Estacionamiento"
     - "☀️ Día caluroso - Prueba bebidas frías y helados"

8. **Estadísticas de Uso**
   - Total de solicitudes realizadas
   - Hits de caché (cuántas veces se usó el caché)
   - Llamadas a API (cuántas veces se consultó la API real)
   - Errores encontrados
   - Eficiencia del caché (porcentaje)

9. **Métodos Adicionales**
   - `obtenerEstadisticas()`: Muestra estadísticas de uso
   - `limpiarCache()`: Limpia el caché manualmente
   - `toStringCompact()`: Versión compacta de la información

10. **Documentación Javadoc Completa**
    - Todas las clases documentadas
    - Todos los métodos documentados
    - Ejemplos de uso incluidos
    - Formato profesional

## 📁 Archivos del Proyecto

### Código Principal
- **`src/teoremadelsabor/api/ServicioClima.java`** (409 líneas)
  - Clase principal que implementa toda la funcionalidad
  - Clase interna `ClimaInfo` para encapsular datos
  - Métodos para obtener clima, estadísticas y gestión de caché

### Pruebas y Demos
- **`src/teoremadelsabor/tests/ServicioClimaTest.java`** (288 líneas)
  - 7 pruebas unitarias exhaustivas
  - Framework de testing personalizado
  - Cobertura completa de funcionalidades

- **`src/teoremadelsabor/tests/DemoServicioClima.java`** (150 líneas)
  - Demostración interactiva de todas las características
  - 8 demos diferentes
  - Formateo elegante de salida

### Integración con el Proyecto
- **`src/teoremadelsabor/main/Main.java`**
  - Muestra el clima al iniciar el programa
  - Integrado en el flujo principal

- **`src/teoremadelsabor/mvc/Vista.java`**
  - Opción en el menú para consultar el clima
  - Muestra información formateada al usuario

## 🧪 Pruebas Unitarias

El proyecto incluye 7 pruebas unitarias completas:

1. **Test 1: Obtener información básica del clima**
   - Verifica que se puede obtener datos de la API
   - Valida que el objeto retornado no es null

2. **Test 2: Verificar funcionamiento del caché**
   - Primera llamada: debe consultar la API
   - Segunda llamada: debe usar el caché
   - Verifica el flag `fromCache`

3. **Test 3: Verificar integridad de campos**
   - Valida que todos los campos estén presentes
   - Verifica que ningún campo sea null

4. **Test 4: Verificar generación de recomendación**
   - Valida que se genere una recomendación
   - Verifica que no esté vacía

5. **Test 5: Verificar estadísticas del servicio**
   - Valida que se generen correctamente
   - Verifica el formato de salida

6. **Test 6: Verificar limpieza de caché**
   - Limpia el caché manualmente
   - Verifica que la siguiente llamada consulte la API

7. **Test 7: Múltiples solicitudes consecutivas**
   - Realiza 5 solicitudes seguidas
   - Mide la eficiencia del caché
   - Calcula estadísticas

### Ejecutar las Pruebas

```bash
# Compilar
javac -d bin -cp src src/teoremadelsabor/**/*.java

# Ejecutar tests
java -cp bin teoremadelsabor.tests.ServicioClimaTest

# Resultado esperado: ✅ 7/7 tests pasados
```

## 🎯 Demo Interactiva

```bash
# Ejecutar la demostración completa
java -cp bin teoremadelsabor.tests.DemoServicioClima
```

La demo muestra:
1. Primera consulta (llamada a API)
2. Segunda consulta (uso de caché)
3. Estadísticas del servicio
4. Test de rendimiento (10 consultas rápidas)
5. Estadísticas actualizadas
6. Limpieza de caché
7. Información detallada del clima
8. Estadísticas finales

## 💻 Ejemplo de Uso

```java
// Obtener información del clima
ClimaInfo clima = ServicioClima.obtenerClimaInfo();

// Mostrar información completa
System.out.println(clima);

// Mostrar versión compacta
System.out.println(clima.toStringCompact());

// Acceder a campos individuales
System.out.println("Temperatura: " + clima.temperatura);
System.out.println("Humedad: " + clima.humedad);
System.out.println("Recomendación: " + clima.recomendacion);

// Ver estadísticas
System.out.println(ServicioClima.obtenerEstadisticas());

// Limpiar caché si es necesario
ServicioClima.limpiarCache();
```

## 🔧 Configuración Técnica

### Timeouts
- **Connect Timeout**: 5000ms (5 segundos)
- **Read Timeout**: 5000ms (5 segundos)

### Caché
- **Duración**: 15 minutos
- **Estrategia**: Última consulta válida
- **Fallback**: Datos antiguos si la API falla

### Logging
- **Framework**: `java.util.logging`
- **Niveles utilizados**: INFO, WARNING, SEVERE, FINE

## 📊 Salida de Ejemplo

```
╔══════════════════════════════════════════════════════════╗
║          🌤️  CLIMA EN CIUDAD DE MÉXICO 🌤️               ║
╠══════════════════════════════════════════════════════════╣
║ Condición:     Sunny                                     ║
║ Temperatura:   25°C                                      ║
║ Sensación:     25°C                                      ║
║ Humedad:       29%                                       ║
║ Viento:        5 km/h                                    ║
║ Precipitación: 0.0 mm                                    ║
╠══════════════════════════════════════════════════════════╣
║ 💡 🌤️ Clima perfecto - Disfruta al aire libre en...     ║
╠══════════════════════════════════════════════════════════╣
║ Actualizado: 18:13:56                           [EN VIVO]║
╚══════════════════════════════════════════════════════════╝
```

## 🎓 Conceptos de Programación Demostrados

1. **Programación Orientada a Objetos**
   - Encapsulación de datos en clase `ClimaInfo`
   - Métodos estáticos para singleton pattern
   - Inmutabilidad de datos (campos `final`)

2. **Manejo de Excepciones**
   - Try-catch-finally apropiado
   - Diferentes tipos de excepciones
   - Logging de errores

3. **Expresiones Regulares**
   - Parseo de JSON sin librerías externas
   - Grupos de captura
   - Patrones flexibles

4. **Caching y Optimización**
   - Reducción de llamadas a red
   - Validación de caché por tiempo
   - Métricas de eficiencia

5. **HTTP y Redes**
   - Uso de `HttpURLConnection`
   - Configuración de headers
   - Manejo de códigos de respuesta

6. **Testing**
   - Framework de assertions personalizado
   - Pruebas unitarias completas
   - Validación de resultados

## 🏆 Ventajas de esta Implementación

1. **Sin dependencias externas**: Todo implementado con Java estándar
2. **Robusto**: Manejo completo de errores y edge cases
3. **Eficiente**: Sistema de caché reduce llamadas a la API
4. **Profesional**: Documentación, logging y testing completos
5. **Educativo**: Código claro y bien comentado
6. **Integrado**: Se usa en el proyecto principal de forma natural
7. **Extensible**: Fácil de modificar y extender

## 📝 Notas de Implementación

- La API wttr.in es gratuita y no requiere autenticación
- Los datos se actualizan cada ~15 minutos en el servidor
- El formato JSON es estable y bien documentado
- Se incluye User-Agent personalizado para identificar la aplicación
- Los emojis en las recomendaciones mejoran la experiencia del usuario

## 🔗 Referencias

- API utilizada: [wttr.in](https://wttr.in)
- Documentación: [wttr.in/:help](https://wttr.in/:help)
- Formato JSON: [wttr.in/Mexico_City?format=j1](https://wttr.in/Mexico_City?format=j1)

---

**Autor**: Teorema del Sabor Team
**Versión**: 2.0
**Fecha**: Noviembre 2025
