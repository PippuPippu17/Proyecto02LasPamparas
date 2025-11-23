# 🐳 Teorema del Sabor - Guía de Docker

> **Sistema de Gestión de Puestos de Comida de la Facultad de Ciencias**
>
> Esta guía explica cómo compilar y ejecutar el proyecto usando Docker en cualquier sistema operativo (Windows, macOS, Linux).

---

## 📋 Tabla de Contenidos

- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
  - [Windows](#-windows)
  - [macOS](#-macos)
  - [Linux](#-linux)
- [Comandos Rápidos](#-comandos-rápidos)
- [Comandos Útiles](#-comandos-útiles)
- [Solución de Problemas](#-solución-de-problemas)
- [Detalles Técnicos](#-detalles-técnicos)

---

## 🔧 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

| Componente | Windows | macOS | Linux |
|------------|---------|-------|-------|
| **Docker Desktop** | ✅ Requerido | ✅ Requerido | ✅ Requerido |
| **Servidor X11** | VcXsrv o Xming | XQuartz | *(Integrado)* |
| **Java 17** *(opcional)* | Para ejecución directa | Para ejecución directa | Para ejecución directa |

### 📥 Enlaces de Descarga

- **Docker Desktop**: [docker.com/get-started](https://www.docker.com/get-started)
- **VcXsrv (Windows)**: [sourceforge.net/projects/vcxsrv](https://sourceforge.net/projects/vcxsrv/)
- **XQuartz (macOS)**: [xquartz.org](https://www.xquartz.org/)

---

## 🚀 Instalación y Configuración

### 🪟 Windows

#### **Paso 1: Instalar VcXsrv para la Interfaz Gráfica**

1. Descarga e instala [VcXsrv](https://sourceforge.net/projects/vcxsrv/)
2. Ejecuta **XLaunch** desde el menú de inicio
3. Selecciona las siguientes opciones:
   - Display settings: **Multiple windows**, Display number: **0**
   - Client startup: **Start no client**
   - Extra settings: ✅ **Disable access control**
4. Haz clic en **Finish**

#### **Paso 2: Construir la Imagen Docker**

```powershell
# En PowerShell o CMD
cd "ruta\al\proyecto\Proyecto02LasPamparas"
docker build -t teorema-del-sabor .
```

#### **Paso 3: Ejecutar el Proyecto**

```powershell
# Con interfaz gráfica (requiere VcXsrv ejecutándose)
docker run -it --rm `
  -e DISPLAY=host.docker.internal:0 `
  -v ${PWD}/data:/app/data `
  -p 8080:8080 `
  teorema-del-sabor

# O usando Docker Compose
docker-compose up
```

---

### 🍎 macOS

#### **Paso 1: Instalar XQuartz**

1. Descarga e instala [XQuartz](https://www.xquartz.org/)
2. Reinicia tu Mac después de la instalación
3. Abre XQuartz desde **Aplicaciones → Utilidades → XQuartz**
4. En las preferencias de XQuartz (XQuartz → Preferences):
   - Pestaña **Security**: ✅ **Allow connections from network clients**

#### **Paso 2: Configurar Permisos X11**

```bash
# En Terminal
export DISPLAY=host.docker.internal:0
xhost +localhost
```

#### **Paso 3: Construir y Ejecutar**

```bash
# Construir la imagen
docker build -t teorema-del-sabor .

# Ejecutar con interfaz gráfica
docker run -it --rm \
  -e DISPLAY=host.docker.internal:0 \
  -v $(pwd)/data:/app/data \
  -p 8080:8080 \
  teorema-del-sabor

# O usando Docker Compose
docker-compose up
```

---

### 🐧 Linux

#### **Paso 1: Permitir Conexiones X11**

```bash
# Dar permisos al servidor X para Docker
xhost +local:docker
```

#### **Paso 2: Construir y Ejecutar**

```bash
# Construir la imagen
docker build -t teorema-del-sabor .

# Ejecutar con interfaz gráfica
docker run -it --rm \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v $(pwd)/data:/app/data \
  -p 8080:8080 \
  teorema-del-sabor

# O usando Docker Compose
docker-compose up
```

---

## ⚡ Comandos Rápidos

### Construcción y Ejecución

```bash
# Construir la imagen por primera vez
docker-compose up --build
# (usa solo Docker Desktop, se requiere tenerlo abierto)

# Ejecutar el proyecto (recomendado - funciona en terminal)
docker-compose run --rm teorema-del-sabor

# Ejecutar con Docker Compose
docker-compose up

# Reconstruir después de cambios en el código
docker-compose up --build

# Ejecutar en segundo plano
docker-compose up -d
```

### Gestión de Contenedores

```bash
# Ver contenedores en ejecución
docker ps

# Ver logs en tiempo real
docker logs -f teorema-del-sabor-app

# Detener el contenedor
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

### Acceso al Contenedor

```bash
# Abrir una terminal dentro del contenedor
docker exec -it teorema-del-sabor-app /bin/bash

# Verificar compilación
docker exec teorema-del-sabor-app ls -la bin/teoremadelsabor/

# Ver versión de Java
docker exec teorema-del-sabor-app java -version
```

---

## 🛠️ Comandos Útiles

### Ejecutar Pruebas Unitarias

```bash
# Ejecutar todas las pruebas dentro del contenedor
docker exec -it teorema-del-sabor-app /bin/bash -c "
  java -cp bin -ea teoremadelsabor.tests.PuestoComidaTest &&
  java -cp bin -ea teoremadelsabor.tests.ServicioClimaTest &&
  java -cp bin -ea teoremadelsabor.tests.StrategyPatternTest &&
  java -cp bin -ea teoremadelsabor.tests.CompositePatternTest
"
```

### Conectarse al Servidor de Notificaciones

```bash
# Usando telnet (desde la máquina host)
telnet localhost 8080

# Usando netcat
nc localhost 8080

# Ejecutar cliente de demostración
java -cp bin teoremadelsabor.main.ClienteDemo
```

### Limpieza de Docker

```bash
# Eliminar contenedores detenidos
docker container prune

# Eliminar imágenes no utilizadas
docker image prune

# Limpieza completa (¡cuidado!)
docker system prune -a

# Eliminar específicamente la imagen del proyecto
docker rmi teorema-del-sabor
```

---

## 🔍 Solución de Problemas

### ❌ La GUI no aparece en Windows

**Problema**: La ventana de la aplicación no se abre

**Soluciones**:

1. **Verificar que VcXsrv está ejecutándose**
   ```powershell
   # Buscar el proceso XLaunch
   Get-Process | Where-Object {$_.Name -like "*vcxsrv*"}
   ```

2. **Verificar el firewall de Windows**
   - Abre **Firewall de Windows Defender**
   - Permite conexiones entrantes para VcXsrv en el puerto 6000

3. **Verificar la variable DISPLAY**
   ```powershell
   docker exec teorema-del-sabor-app echo $DISPLAY
   # Debería mostrar: host.docker.internal:0
   ```

4. **Reiniciar VcXsrv con configuración correcta**
   - Desmarcar "Native opengl"
   - Marcar "Disable access control"

---

### ❌ La GUI no aparece en macOS

**Problema**: Error "Can't connect to X11 window server"

**Soluciones**:

1. **Verificar que XQuartz está ejecutándose**
   ```bash
   ps aux | grep XQuartz
   ```

2. **Dar permisos explícitos**
   ```bash
   xhost +localhost
   xhost +127.0.0.1
   xhost +$(hostname)
   ```

3. **Verificar la variable DISPLAY**
   ```bash
   echo $DISPLAY
   # Debería mostrar algo como: /private/tmp/com.apple.launchd.xxx/org.xquartz:0
   ```

4. **Reiniciar XQuartz**
   ```bash
   killall XQuartz
   open -a XQuartz
   ```

---

### ❌ La GUI no aparece en Linux

**Problema**: Error "Cannot open display"

**Soluciones**:

1. **Dar permisos al servidor X**
   ```bash
   xhost +local:docker
   # o más permisivo (menos seguro):
   xhost +
   ```

2. **Verificar DISPLAY**
   ```bash
   echo $DISPLAY
   # Normalmente: :0 o :1
   ```

3. **Verificar que X11 está corriendo**
   ```bash
   ps aux | grep X
   ```

---

### ❌ Error de compilación

**Problema**: El contenedor no compila correctamente

**Soluciones**:

```bash
# Reconstruir sin caché
docker build --no-cache -t teorema-del-sabor .

# O con Docker Compose
docker-compose build --no-cache
docker-compose up
```

---

### ❌ No se guardan los cambios en puestos.txt

**Problema**: Los datos no persisten después de cerrar el contenedor

**Soluciones**:

1. **Verificar el montaje del volumen**
   ```bash
   docker inspect teorema-del-sabor-app | grep Mounts -A 10
   ```

2. **Verificar permisos del directorio data/**
   ```bash
   # Linux/macOS
   ls -la data/
   chmod 755 data/

   # Windows (PowerShell como administrador)
   icacls data /grant Everyone:F
   ```

---

### ❌ Puerto 8080 ya está en uso

**Problema**: Error "port is already allocated"

**Soluciones**:

```bash
# Ver qué está usando el puerto 8080
# Windows
netstat -ano | findstr :8080

# Linux/macOS
lsof -i :8080

# Cambiar el puerto en docker-compose.yml
# Editar la línea: "8081:8080" (usa 8081 en el host)
```

---

## 📊 Detalles Técnicos

### Estructura de la Imagen Docker

```dockerfile
FROM eclipse-temurin:17-jdk-slim
# Imagen base: Java 17 (Eclipse Temurin - distribución oficial)
# Tamaño: ~200 MB (versión slim)

WORKDIR /app
# Directorio de trabajo dentro del contenedor

RUN apt-get update && apt-get install -y \
    libxext6 libxrender1 libxtst6 libxi6 libfreetype6 fontconfig
# Dependencias necesarias para Java Swing (GUI)

COPY . .
# Copiar todo el proyecto al contenedor

RUN javac -d bin -sourcepath src src/teoremadelsabor/main/*.java
# Compilar el proyecto automáticamente al construir la imagen
```

### Volúmenes Montados

| Ruta en Contenedor | Ruta en Host | Propósito |
|-------------------|--------------|-----------|
| `/app/data` | `./data` | Persistencia de datos (puestos.txt, .tmp, reportes) |
| `/tmp/.X11-unix` | `/tmp/.X11-unix` | Socket X11 (solo Linux) |

### Puertos Expuestos

| Puerto | Protocolo | Servicio |
|--------|-----------|----------|
| **8080** | TCP | Servidor de notificaciones (Sockets) |

### Variables de Entorno

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `DISPLAY` | `host.docker.internal:0` (Win/Mac)<br>`$DISPLAY` (Linux) | Servidor X11 para la GUI |
| `JAVA_HOME` | `/opt/java/openjdk` | Ubicación de Java en el contenedor |

---

## 🎯 Alternativas de Ejecución

### Sin Docker (Ejecución Directa)

Si prefieres ejecutar sin Docker:

```bash
# 1. Compilar
javac -d bin -sourcepath src src/teoremadelsabor/main/*.java

# 2. Ejecutar aplicación principal
java -cp bin teoremadelsabor.main.Main

# 3. Ejecutar pruebas unitarias
java -cp bin -ea teoremadelsabor.tests.PuestoComidaTest
java -cp bin -ea teoremadelsabor.tests.StrategyPatternTest
java -cp bin -ea teoremadelsabor.tests.CompositePatternTest
java -cp bin -ea teoremadelsabor.tests.ServicioClimaTest

# 4. Ejecutar cliente de notificaciones
java -cp bin teoremadelsabor.main.ClienteDemo
```

### Solo Verificar Compilación (Sin GUI)

```bash
# Verificar que el proyecto compila correctamente
docker run -it --rm teorema-del-sabor java -version

# Listar archivos compilados
docker run -it --rm teorema-del-sabor ls -la bin/teoremadelsabor/

# Ejecutar pruebas unitarias
docker run -it --rm teorema-del-sabor \
  java -cp bin -ea teoremadelsabor.tests.PuestoComidaTest
```

---

## 📝 Notas Adicionales

- ✅ **Portabilidad**: Docker garantiza que el proyecto funciona igual en Windows, macOS y Linux
- ✅ **Compilación automática**: La imagen se compila al construirse, asegurando que no hay errores
- ✅ **Persistencia**: Los datos en `data/` se guardan en tu máquina local, no se pierden
- ✅ **Aislamiento**: El proyecto corre en un entorno aislado sin afectar tu sistema
- ⚠️ **GUI requiere configuración**: Windows y macOS necesitan un servidor X11 para mostrar la interfaz
- 🔒 **Seguridad**: Usa `xhost +local:docker` en lugar de `xhost +` para mayor seguridad

---

## 🆘 ¿Necesitas Ayuda?

Si encuentras problemas no cubiertos en esta guía:

1. Revisa los logs del contenedor: `docker logs teorema-del-sabor-app`
2. Verifica que Docker Desktop está ejecutándose
3. Asegúrate de estar en el directorio correcto del proyecto
4. Consulta la documentación oficial de Docker: [docs.docker.com](https://docs.docker.com)

---

**🎓 Proyecto desarrollado para la materia de Modelado y Programación**
**Facultad de Ciencias, UNAM**
