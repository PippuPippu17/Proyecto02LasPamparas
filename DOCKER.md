# Teorema del Sabor - Guía de Docker

Esta guía explica cómo compilar y ejecutar el proyecto "Teorema del Sabor" usando Docker en cualquier sistema operativo.

## ⚠️ IMPORTANTE: Interfaz Gráfica en Windows

**La GUI (interfaz gráfica) NO funciona nativamente en Docker para Windows.**

Para ver la interfaz gráfica tienes 2 opciones:

### **Opción A (Recomendada): Ejecutar directamente con Java**
```bash
javac -d bin -sourcepath src src/teoremadelsabor/main/*.java
java -cp bin teoremadelsabor.main.Main
```

### **Opción B: Usar Docker solo para demostrar que compila**
Docker garantiza que el proyecto compila en cualquier sistema, pero para ver la GUI necesitas Java local.

---

## Requisitos Previos

- Docker instalado ([Descargar Docker](https://www.docker.com/get-started))
- Docker Compose (incluido con Docker Desktop)
- **Para GUI en Windows**: Java 17 instalado localmente

## Construcción de la Imagen

### Paso 1: Construir la imagen Docker

```bash
docker build -t teorema-del-sabor .
```

Este comando:
- Lee el `Dockerfile`
- Instala Java 17 y dependencias GUI
- Compila todo el proyecto
- Crea una imagen llamada `teorema-del-sabor`

## Ejecución del Proyecto

### En Linux/macOS

```bash
# Permitir conexiones X11
xhost +local:docker

# Ejecutar con Docker Compose
docker-compose up

# O ejecutar directamente
docker run -it --rm \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v $(pwd)/data:/app/data \
  -p 8080:8080 \
  teorema-del-sabor
```

### En Windows

**Opción 1: Usar WSL2 (Recomendado)**

```powershell
# En PowerShell
docker run -it --rm `
  -v ${PWD}/data:/app/data `
  -p 8080:8080 `
  teorema-del-sabor
```

**Opción 2: Usar VcXsrv (Para GUI)**

1. Descargar e instalar [VcXsrv](https://sourceforge.net/projects/vcxsrv/)
2. Ejecutar XLaunch con las opciones por defecto
3. Ejecutar el contenedor:

```powershell
docker run -it --rm `
  -e DISPLAY=host.docker.internal:0 `
  -v ${PWD}/data:/app/data `
  -p 8080:8080 `
  teorema-del-sabor
```

## Comandos Útiles

### Ver logs del contenedor
```bash
docker logs teorema-del-sabor-app
```

### Detener el contenedor
```bash
docker-compose down
```

### Reconstruir la imagen después de cambios
```bash
docker-compose up --build
```

### Ejecutar bash dentro del contenedor
```bash
docker exec -it teorema-del-sabor-app /bin/bash
```

### Limpiar imágenes y contenedores
```bash
# Detener todos los contenedores
docker stop $(docker ps -aq)

# Eliminar contenedores detenidos
docker rm $(docker ps -aq)

# Eliminar imagen
docker rmi teorema-del-sabor
```

## Estructura de Volúmenes

El contenedor monta el directorio `data/` localmente para persistir:
- `puestos.txt` - Datos de los puestos
- Archivos `.tmp` - Estados temporales
- Reportes generados

## Puertos Expuestos

- **8080**: Puerto del servidor de notificaciones (Sockets)

## Solución de Problemas

### La GUI no aparece en Linux/macOS
```bash
# Verificar que X11 permite conexiones
xhost +local:docker

# Verificar la variable DISPLAY
echo $DISPLAY
```

### La GUI no aparece en Windows
- Asegurarse de que VcXsrv está corriendo
- Verificar que el firewall permite conexiones en el puerto 6000
- Usar `host.docker.internal:0` como DISPLAY

### Error de compilación
```bash
# Limpiar y reconstruir
docker-compose down
docker-compose build --no-cache
docker-compose up
```

### No se pueden guardar cambios en puestos.txt
- Verificar que el volumen está montado correctamente
- Verificar permisos del directorio `data/`

## Alternativa: Ejecutar sin GUI

Si solo quieres probar la compilación sin GUI:

```bash
docker run -it --rm teorema-del-sabor java -version
```

## Verificar la Compilación

Para verificar que todo compiló correctamente:

```bash
docker run -it --rm teorema-del-sabor ls -la bin/teoremadelsabor/
```

Deberías ver todos los archivos `.class` compilados organizados por paquetes.

## Notas Adicionales

- La imagen base es `openjdk:17-jdk-slim` (ligera y eficiente)
- Incluye todas las dependencias necesarias para Java Swing
- El proyecto se compila automáticamente al construir la imagen
- Los datos persisten en el directorio `data/` local
