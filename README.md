# Teorema del Sabor - Sistema de Puestos de Comida

Sistema de gestión de puestos de comida de la Facultad de Ciencias, UNAM.

## Patrones de Diseño Implementados

- **MVC** (Model-View-Controller)
- **Observer** (Notificaciones)
- **State** (Estados de puestos)
- **Strategy** (Métodos de pago)
- **Composite** (Estructura de puestos)
- **Factory** (Creación de objetos)
- **Threads** (Actualizador de estados)
- **Sockets** (Servidor de notificaciones)

## Requisitos

### Para ejecutar con interfaz gráfica (Recomendado):
- **Java 17** o superior

### Para verificar portabilidad (Docker):
- **Docker Desktop** instalado
  - [Descargar Docker Desktop](https://www.docker.com/get-started)

## ⚡ Ejecución Rápida (con GUI)

### 1. Compilar el proyecto

```bash
javac -d bin -sourcepath src src/teoremadelsabor/main/*.java
```

### 2. Ejecutar el proyecto

```bash
java -cp bin teoremadelsabor.main.Main
```

### 3. Seleccionar interfaz

Cuando aparezca el menú, escribe `1` para abrir la interfaz gráfica.

---

## 🐳 Ejecución con Docker

**Nota:** Docker compila y ejecuta el proyecto, pero la GUI **NO se muestra en Windows**.
Usar Docker para demostrar que el proyecto es portable y compila en cualquier sistema.

### 1. Construir la imagen

```bash
docker build -t teorema-del-sabor .
```

### 2. Verificar que compila

```bash
docker-compose up --build
```

Verás en los logs que el proyecto compila y ejecuta correctamente (sin GUI visible).

---

## Ejecución paso a paso con Java

### 1. Compilar el proyecto

```bash
javac -d bin -sourcepath src src/teoremadelsabor/main/*.java
```

### 2. Ejecutar el proyecto

```bash
java -cp bin teoremadelsabor.main.Main
```

## Características del Sistema

### Funcionalidades

- ✅ Visualización de 22 puestos de comida
- ✅ Filtros múltiples (Tipo, Ubicación, Precio)
- ✅ Información detallada de cada puesto
- ✅ Información del clima en tiempo real (Open-Meteo API)
- ✅ Estados dinámicos (Abierto, Cerrado, En Descanso)
- ✅ Servidor de notificaciones en puerto 8080
- ✅ Persistencia de datos en `data/puestos.txt`

### Filtros Disponibles

- **Tipo**: Permite seleccionar hasta 2 tipos de comida simultáneamente
  - Comida Completa, Comida Rápida, Tacos, Snacks, Postres, Golosinas, Asiática, Snacks y Dulces
- **Ubicación**: Comedor, Estacionamiento, Media Luna, Puestos, Edificio O
- **Precio máximo**: Filtro por precio promedio

## Estructura del Proyecto

```
Proyecto02LasPamparas/
├── data/
│   └── puestos.txt          # Datos de los puestos
├── src/
│   └── teoremadelsabor/
│       ├── main/            # Punto de entrada
│       ├── mvc/             # Patrón MVC
│       ├── gui/             # Interfaz gráfica (Swing)
│       ├── state/           # Patrón State
│       ├── strategy/        # Patrón Strategy
│       ├── observer/        # Patrón Observer
│       ├── composite/       # Patrón Composite
│       ├── factory/         # Patrón Factory
│       ├── threads/         # Hilos de actualización
│       ├── sockets/         # Servidor de notificaciones
│       ├── api/             # Integración con API del clima
│       └── persistencia/    # Generación de reportes
├── Dockerfile               # Configuración de Docker
├── docker-compose.yml       # Orquestación de contenedores
└── README.md               # Este archivo

```

## Comandos Útiles de Docker

### Ver logs del contenedor
```bash
docker logs teorema-del-sabor-app
```

### Reconstruir la imagen
```bash
docker-compose up --build
```

### Limpiar contenedores e imágenes
```bash
docker-compose down
docker rmi teorema-del-sabor
```

## Notas Técnicas

- **Puerto 8080**: Servidor de notificaciones (Sockets)
- **API del Clima**: Open-Meteo (Ciudad de México)
- **Actualización automática**: Estados de puestos se actualizan cada 60 segundos
- **Persistencia**: Los datos se guardan en `data/puestos.txt`

## Autores

Proyecto desarrollado para la materia de **Modelado y Programación**
Facultad de Ciencias, UNAM
Semestre 2025-1

## 🖥️ Sobre Docker y la Interfaz Gráfica

### **En Windows:**
- ❌ Docker **NO puede mostrar** la interfaz gráfica
- ✅ Docker **SÍ compila y ejecuta** el proyecto (demuestra portabilidad)
- 💡 **Recomendación**: Usa Java directamente para ver la GUI

### **En Linux/macOS:**
- ✅ Docker **SÍ puede mostrar** la GUI usando X11
- 🚀 Ejecuta: `./run-docker-gui.sh` (script incluido)

### **¿Cuál usar para entregar el proyecto?**

**Incluye ambas opciones:**
1. **Java directo** → Para que el profesor vea la interfaz gráfica
2. **Docker** → Para demostrar que el proyecto es portable y compila en cualquier sistema

## Documentación Adicional

- [Guía completa de Docker](DOCKER.md) - Instrucciones detalladas para diferentes sistemas operativos
