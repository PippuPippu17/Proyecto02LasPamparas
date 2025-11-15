"""
Ejemplo de uso del sistema de lámparas
"""

import sys
import os

# Agregar el directorio src al path para importar
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'src'))

from lampara import Lampara


def ejemplo_basico():
    """Ejemplo básico de uso de una lámpara"""
    print("=== Ejemplo Básico ===\n")
    
    lampara = Lampara("LED", 100)
    print(f"1. Lámpara creada: {lampara}")
    
    lampara.encender()
    print(f"2. Lámpara encendida: {lampara}")
    
    lampara.ajustar_intensidad(75)
    print(f"3. Intensidad ajustada: {lampara}")
    
    lampara.apagar()
    print(f"4. Lámpara apagada: {lampara}\n")


def ejemplo_multiples_lamparas():
    """Ejemplo con múltiples lámparas de diferentes tipos"""
    print("=== Ejemplo con Múltiples Lámparas ===\n")
    
    lamparas = [
        Lampara("LED", 100),
        Lampara("Incandescente", 80),
        Lampara("Fluorescente", 90)
    ]
    
    print("Encendiendo todas las lámparas:")
    for lampara in lamparas:
        lampara.encender()
        print(f"  - {lampara}")
    
    print("\nAjustando intensidades:")
    for i, lampara in enumerate(lamparas):
        intensidad = 30 + (i * 20)
        lampara.ajustar_intensidad(intensidad)
        print(f"  - {lampara}")
    
    print("\nApagando todas las lámparas:")
    for lampara in lamparas:
        lampara.apagar()
        print(f"  - {lampara}")


def ejemplo_control_intensidad():
    """Ejemplo de control de intensidad"""
    print("\n=== Ejemplo de Control de Intensidad ===\n")
    
    lampara = Lampara("LED", 100)
    lampara.encender()
    
    print("Incrementando intensidad gradualmente:")
    for intensidad in range(0, 101, 25):
        lampara.ajustar_intensidad(intensidad)
        print(f"  Intensidad al {intensidad}%: {lampara}")


if __name__ == "__main__":
    print("=" * 60)
    print("EJEMPLOS DE USO DEL SISTEMA DE LÁMPARAS")
    print("=" * 60 + "\n")
    
    ejemplo_basico()
    ejemplo_multiples_lamparas()
    ejemplo_control_intensidad()
    
    print("\n" + "=" * 60)
    print("Ejemplos completados exitosamente")
    print("=" * 60)
