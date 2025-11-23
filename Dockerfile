# Imagen base: OpenJDK 17 oficial (Eclipse Temurin)
FROM eclipse-temurin:17-jdk

# Instalar librerías necesarias para AWT/Swing + X11
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libx11-6 \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

# DISPLAY apunta al servidor X de Windows (VcXsrv)
ENV DISPLAY=host.docker.internal:0.0

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar todo el proyecto al contenedor
COPY . /app

# Crear directorio de salida para .class
RUN mkdir -p bin

# Compilar TODAS las clases Java (recorriendo src completo)
RUN find src -name "*.java" > sources.txt && \
    javac -d bin @sources.txt

# Exponer el puerto que usas para el servidor (notificaciones/clima)
EXPOSE 8080

# Punto de entrada: ejecuta Main (mostrará el menú con las 2 opciones)
CMD ["java", "-cp", "bin", "teoremadelsabor.main.Main"]
