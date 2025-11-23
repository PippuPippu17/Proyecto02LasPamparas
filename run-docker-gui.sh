#!/bin/bash

# Script para ejecutar Teorema del Sabor con GUI en Linux/macOS

echo "======================================"
echo "Teorema del Sabor - Ejecución con GUI"
echo "======================================"
echo ""

# Verificar que Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado"
    echo "Descarga Docker desde: https://www.docker.com/get-started"
    exit 1
fi

echo "✅ Docker detectado"
echo ""

# Permitir conexiones X11
echo "🔧 Configurando permisos X11..."
xhost +local:docker

# Construir la imagen
echo ""
echo "🏗️  Construyendo imagen Docker..."
docker build -t teorema-del-sabor .

if [ $? -ne 0 ]; then
    echo "❌ Error al construir la imagen"
    exit 1
fi

echo ""
echo "✅ Imagen construida exitosamente"
echo ""

# Ejecutar el contenedor con GUI
echo "🚀 Ejecutando Teorema del Sabor..."
echo ""

docker run -it --rm \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v "$(pwd)/data:/app/data" \
  -p 8080:8080 \
  --name teorema-del-sabor-app \
  teorema-del-sabor

# Limpiar permisos X11
xhost -local:docker

echo ""
echo "👋 Aplicación cerrada"
