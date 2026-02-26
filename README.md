# Wordle Java Edition 🧩

Este es un simulador del popular juego **Wordle** ejecutado directamente en la consola. 

## ✨ Características
***Gestión de Ficheros:** Lectura de palabras secretas desde `listadepalabras.txt`.
**Persistencia:** Guardado automático del historial de intentos y resultados en `historialdepartidas.txt`.
***Feedback Visual:** Uso de códigos de colores ANSI para indicar el estado de las letras (Correcta, Posición Incorrecta, Incorrecta).
**Validación de Entrada:** Control de longitud de palabras (5 letras) y manejo de mayúsculas/minúsculas.

## 🚀 Cómo ejecutar
1. Clona el repositorio.
2. Asegúrate de tener instalado el JDK (Java Development Kit).
3. Compila y ejecuta la clase principal `TrabajoDeEnfoqueProgramacion`.
4. ¡Adivina la palabra en menos de 6 intentos!

## 🛠️ Tecnologías utilizadas
* **Lenguaje:** Java
* **IDE:** NetBeans (con soporte para proyectos Maven/Ant) 
* **Entrada/Salida:** Clases `Scanner`, `BufferedReader` y `PrintWriter.

## 📋 Próximas mejoras
*  Añadir un temporizador para medir el tiempo de resolución.
* Implementar sonidos al ganar o perder.
*  Permitir jugar partidas consecutivas sin reiniciar la aplicación (código comentado en archivo principal)
