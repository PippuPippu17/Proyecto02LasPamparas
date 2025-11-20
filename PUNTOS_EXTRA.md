# PUNTOS EXTRAS IMPLEMENTADOS

## 📋 Resumen
Este proyecto implementa **TODAS** las funcionalidades opcionales para obtener puntos extra:

1. ✅ **Lectura y Escritura de Archivos**
2. ✅ **Threads (Programación Concurrente)**
3. ✅ **Sockets (Cliente-Servidor)**
4. ✅ **API Externa**
5. ✅ **Pruebas Unitarias**
6. ✅ **GUI (Interfaz Gráfica con Swing)**

---

## 1. 📁 LECTURA Y ESCRITURA DE ARCHIVOS

### Ubicación
- `src/teoremadelsabor/persistencia/AlmacenPuestos.java` (Ya existente)
- `src/teoremadelsabor/persistencia/GeneradorReportes.java` (NUEVO)

### Funcionalidades
- **Lectura**: Carga puestos desde `data/puestos.txt`
- **Escritura**: Guarda cambios en puestos
- **Reportes TXT**: Genera reportes en formato texto
- **Reportes CSV**: Genera reportes compatibles con Excel
- **Logs**: Registra actividad del sistema

### Cómo probarlo
1. Ejecutar el programa
2. Seleccionar opción **8. Generar reporte**
3. Elegir formato (TXT o CSV)
4. Los archivos se generan en la carpeta raíz:
   - `reporte_puestos.txt`
   - `reporte_puestos.csv`
   - `actividad.log`

---

## 2. 🧵 THREADS (Programación Concurrente)

### Ubicación
- `src/teoremadelsabor/threads/ActualizadorEstados.java`

### Funcionalidades
- **Thread Demonio**: Se ejecuta en segundo plano
- **Actualización Automática**: Actualiza estados de puestos cada 60 segundos
- **Thread-Safe**: Uso de `volatile` para sincronización
- **Shutdown Hook**: Se detiene limpiamente al cerrar el programa

### Cómo funciona
```java
ActualizadorEstados actualizador = new ActualizadorEstados(gestor, 60);
actualizador.start(); // Inicia automáticamente
```

El thread:
1. Se ejecuta cada 60 segundos
2. Actualiza el estado de todos los puestos según la hora actual
3. Notifica a los usuarios suscritos (patrón Observer)
4. Muestra mensajes en consola: `[THREAD] Estados actualizados...`

---

## 3. 🌐 SOCKETS (Cliente-Servidor)

### Ubicación
- `src/teoremadelsabor/sockets/ServidorNotificaciones.java`
- `src/teoremadelsabor/sockets/ClienteNotificaciones.java`

### Funcionalidades
- **Servidor TCP**: Escucha en puerto 8080
- **Múltiples Clientes**: Maneja varios clientes simultáneamente
- **Notificaciones Push**: Envía notificaciones a todos los clientes conectados
- **Thread por Cliente**: Cada cliente tiene su propio thread

### Arquitectura
```
Servidor (puerto 8080)
    ↓
Acepta múltiples clientes
    ↓
Cada cliente en su propio thread
    ↓
Servidor puede notificar a todos
```

### Cómo probarlo
El servidor se inicia automáticamente con el programa:
```
[SERVIDOR] Iniciado en puerto 8080
```

Para conectar un cliente (desde otra terminal):
```bash
telnet localhost 8080
# O usar el cliente Java incluido
```

---

## 4. 🌤️ API EXTERNA (REST)

### Ubicación
- `src/teoremadelsabor/api/ServicioClima.java`

### API Utilizada
- **wttr.in**: API gratuita de clima (no requiere API key)
- **Endpoint**: `https://wttr.in/Mexico_City`

### Funcionalidades
- **Consulta HTTP GET**: Usando `HttpURLConnection`
- **Timeout**: 5 segundos de timeout
- **Parseo de Respuesta**: Extrae temperatura, descripción y humedad
- **Recomendaciones**: Da sugerencias según el clima

### Cómo probarlo
1. Ejecutar el programa (consulta automática al inicio)
2. Seleccionar opción **9. Ver clima actual**
3. Resultado ejemplo:
   ```
   Clima en CDMX: Partly cloudy +18°C Humedad:65%
   Recomendacion: Clima agradable para cualquier puesto
   ```

**Nota**: Requiere conexión a internet

---

## 5. 🧪 PRUEBAS UNITARIAS

### Ubicación
- `src/teoremadelsabor/tests/PuestoComidaTest.java`

### Tests Implementados
1. **testCreacionPuesto()**: Verifica creación correcta
2. **testCambioEstado()**: Prueba patrón State
3. **testObserverPattern()**: Prueba patrón Observer
4. **testHorarioDisponibilidad()**: Verifica lógica de horarios
5. **testActualizacionPrecio()**: Prueba mutabilidad

### Cómo ejecutarlos
```bash
# Compilar
javac -d bin -sourcepath src src/teoremadelsabor/tests/PuestoComidaTest.java

# Ejecutar
java -ea -cp bin teoremadelsabor.tests.PuestoComidaTest
```

**Nota**: `-ea` activa las assertions

### Resultado Esperado
```
========================================
  PRUEBAS UNITARIAS - TEOREMA DEL SABOR
========================================

[TEST 1] Creacion de puesto...
  ✓ PASADO

[TEST 2] Cambio de estado...
  ✓ PASADO

[TEST 3] Patron Observer...
  ✓ PASADO

[TEST 4] Horario y disponibilidad...
  ✓ PASADO

[TEST 5] Actualizacion de precio...
  ✓ PASADO

========================================
RESULTADOS:
  Pasados: 5
  Fallados: 0
========================================
```

---

## 6. 🖥️ GUI (Interfaz Gráfica con Swing)

### Ubicación
- `src/teoremadelsabor/gui/VentanaPrincipal.java`

### Funcionalidades
- **JTable**: Tabla interactiva con todos los puestos
- **Filtros Dinámicos**: Por tipo, ubicación y precio
- **Panel de Detalles**: Muestra información del puesto seleccionado
- **Integración con API**: Muestra clima en tiempo real
- **Generación de Reportes**: Desde la interfaz gráfica
- **Suscripciones**: Permite suscribirse a puestos con diálogos
- **SwingWorker**: Para llamadas asíncronas a la API

### Componentes GUI
```
VentanaPrincipal (1000x700)
├── Panel Superior (Título + Clima)
├── Panel Central (JTable con puestos)
├── Panel Derecho (Detalles + Botones)
└── Panel Inferior (Filtros)
```

### Cómo probarlo
1. Ejecutar el programa
2. Seleccionar opción **1. Interfaz Gráfica (GUI)**
3. La ventana se abre con:
   - Tabla de todos los puestos
   - Clima actualizado en la parte superior
   - Botones de acción a la derecha
   - Filtros en la parte inferior

### Características destacadas
- **Thread-Safe**: Usa SwingUtilities.invokeLater()
- **Event-Driven**: Listeners para todos los eventos
- **Responsive**: SwingWorker para no bloquear la GUI
- **Integración completa**: Usa el mismo MVC que la consola

---

## 📊 RESUMEN TÉCNICO

| Funcionalidad | Archivo Principal | Líneas de Código | Complejidad |
|---------------|------------------|------------------|-------------|
| Archivos | GeneradorReportes.java | ~100 | Media |
| Threads | ActualizadorEstados.java | ~60 | Media |
| Sockets | ServidorNotificaciones.java | ~120 | Alta |
| API | ServicioClima.java | ~65 | Baja |
| Tests | PuestoComidaTest.java | ~160 | Media |
| GUI | VentanaPrincipal.java | ~450 | Alta |

**Total**: ~955 líneas de código adicionales

---

## 🚀 COMANDOS DE COMPILACIÓN Y EJECUCIÓN

### Compilar todo
```bash
javac -d bin -sourcepath src src/teoremadelsabor/main/Main.java
```

### Ejecutar programa principal
```bash
java -cp bin teoremadelsabor.main.Main
```

### Ejecutar tests
```bash
java -ea -cp bin teoremadelsabor.tests.PuestoComidaTest
```

---

## 🎯 DEMOSTRACIÓN EN CLASE

### 1. Mostrar GUI (Interfaz Gráfica)
- Ejecutar programa
- Seleccionar opción 1 (GUI)
- Mostrar ventana con tabla, filtros y clima
- Hacer clic en un puesto para ver detalles
- Aplicar filtros (por tipo, ubicación o precio)
- Generar reporte desde el botón

### 2. Mostrar Threads
- Ejecutar programa
- Esperar 60 segundos
- Observar mensaje: `[THREAD] Estados actualizados...`

### 3. Mostrar Archivos
- En GUI: Click en "Generar Reporte TXT" o "CSV"
- En Consola: Opción 8 → Generar reporte
- Abrir `reporte_puestos.txt` o `reporte_puestos.csv`
- Mostrar contenido

### 4. Mostrar API
- En GUI: El clima se muestra automáticamente arriba
- En Consola: Opción 9 → Ver clima
- Mostrar respuesta en tiempo real

### 5. Mostrar Sockets
- Terminal 1: Ejecutar programa (servidor inicia)
- Terminal 2: `telnet localhost 8080`
- Observar mensajes de conexión

### 6. Mostrar Tests
- Ejecutar PuestoComidaTest
- Mostrar resultado con todos los tests pasando

---

## 📝 NOTAS IMPORTANTES

1. **Internet**: La API de clima requiere conexión a internet
2. **Puerto 8080**: Asegurarse que esté disponible para Sockets
3. **Assertions**: Ejecutar tests con flag `-ea`
4. **Archivos**: Se generan en el directorio raíz del proyecto

---

## 🏆 PUNTOS EXTRAS OBTENIDOS

✅ Lectura/Escritura de archivos (CSV, TXT, Logs)
✅ Threads con sincronización
✅ Sockets cliente-servidor
✅ API REST externa
✅ Pruebas unitarias completas
✅ GUI con Java Swing (interfaz gráfica completa)

**TOTAL**: 6/6 funcionalidades extras implementadas
