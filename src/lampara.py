"""
Clase Lampara - Ejemplo básico
Este archivo contiene una implementación básica de una clase Lámpara
"""


class Lampara:
    """
    Clase que representa una lámpara con funcionalidades básicas
    """
    
    def __init__(self, tipo="LED", intensidad_maxima=100):
        """
        Inicializa una nueva lámpara
        
        Args:
            tipo (str): Tipo de lámpara (LED, incandescente, fluorescente)
            intensidad_maxima (int): Intensidad máxima de la lámpara (0-100)
        """
        self.tipo = tipo
        self.intensidad_maxima = intensidad_maxima
        self.encendida = False
        self.intensidad_actual = 0
    
    def encender(self):
        """Enciende la lámpara"""
        self.encendida = True
        self.intensidad_actual = self.intensidad_maxima
        return True
    
    def apagar(self):
        """Apaga la lámpara"""
        self.encendida = False
        self.intensidad_actual = 0
        return True
    
    def ajustar_intensidad(self, intensidad):
        """
        Ajusta la intensidad de la lámpara
        
        Args:
            intensidad (int): Nueva intensidad (0-intensidad_maxima)
            
        Returns:
            bool: True si se ajustó correctamente, False si no
        """
        if not self.encendida:
            return False
        
        if 0 <= intensidad <= self.intensidad_maxima:
            self.intensidad_actual = intensidad
            return True
        return False
    
    def obtener_estado(self):
        """
        Obtiene el estado actual de la lámpara
        
        Returns:
            dict: Diccionario con el estado de la lámpara
        """
        return {
            'tipo': self.tipo,
            'encendida': self.encendida,
            'intensidad_actual': self.intensidad_actual,
            'intensidad_maxima': self.intensidad_maxima
        }
    
    def __str__(self):
        """Representación en string de la lámpara"""
        estado = "encendida" if self.encendida else "apagada"
        return f"Lámpara {self.tipo} ({estado}): {self.intensidad_actual}/{self.intensidad_maxima}"


if __name__ == "__main__":
    # Ejemplo de uso
    print("=== Ejemplo de uso de la clase Lampara ===\n")
    
    # Crear una lámpara LED
    lampara1 = Lampara("LED", 100)
    print(f"Estado inicial: {lampara1}")
    
    # Encender la lámpara
    lampara1.encender()
    print(f"Después de encender: {lampara1}")
    
    # Ajustar intensidad
    lampara1.ajustar_intensidad(50)
    print(f"Después de ajustar a 50%: {lampara1}")
    
    # Obtener estado completo
    print(f"\nEstado completo: {lampara1.obtener_estado()}")
    
    # Apagar la lámpara
    lampara1.apagar()
    print(f"Después de apagar: {lampara1}")
