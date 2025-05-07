# Messi Flappy Bird 🐐⚽

Versión personalizada del clásico Flappy Bird protagonizada por Lionel Messi. En este juego, el jugador debe esquivar obstáculos y recoger power-ups mientras acumula puntos.

---

## 🎮 Descripción del Juego

Desarrollamos una versión personalizada de Flappy Bird donde el jugador controla a Messi mientras vuela por el aire, esquivando defensores (obstáculos móviles) y tuberías (obstáculos fijos). También se pueden recoger power-ups para obtener ventajas temporales como invencibilidad o reducción de tamaño.

El objetivo del juego es sobrevivir el mayor tiempo posible y alcanzar la mejor puntuación.

---

## Requerimientos Funcionales

- El jugador controla a Messi, quien sube al presionar la flecha arriba.
- El jugador debe esquivar **tuberías fijas**.
- El jugador debe esquivar **defensores móviles**.
- El jugador puede recoger un **Balón de Oro** que otorga invencibilidad temporal.
- El jugador puede recoger una **Bebida Power-Up** que reduce el tamaño de Messi temporalmente.
- El juego detecta colisiones:
  - Con obstáculos fijos o móviles (pierde salvo que esté invencible).
- El puntaje aumenta al pasar el tiempo.
- El puntaje actual se muestra en pantalla durante la partida.
- Al perder, se muestra el puntaje final y se puede reiniciar.

---

## Requerimientos No Funcionales

- El juego debe ejecutarse a 60 FPS para una jugabilidad fluida.
- El código debe estar estructurado de forma orientada a objetos (OOP).
- Las imágenes del juego deben estar correctamente cargadas desde archivos externos.
- El sistema debe reiniciar el juego de forma limpia al perder.
- El diseño debe ser visualmente claro y con una interfaz simple.

---

## ✨ Épica

**"Como equipo desarrollador, queremos crear una versión divertida y desafiante del clásico Flappy Bird protagonizada por Messi, que permita a los jugadores esquivar obstáculos y usar power-ups, con el objetivo de lograr la mayor puntuación posible."**

---

## 📚 Historias de Usuario

### 🎮 Control del personaje
**HU01**  
**Como** jugadores,  
**queremos** que Messi suba al tocar la flecha `↑`,  
**para** poder esquivar los obstáculos mientras cae por gravedad.

### 🚧 Obstáculos fijos
**HU02**  
**Como** jugadores,  
**queremos** que haya tuberías fijas como obstáculos,  
**para** que el juego sea desafiante y nos obligue a maniobrar con precisión.

### 🧍‍♂️ Obstáculos móviles
**HU03**  
**Como** jugadores,  
**queremos** que aparezcan defensores que se mueven horizontalmente,  
**para** aumentar la dificultad y variedad del juego.

### 🏆 Power-Up de invencibilidad
**HU04**  
**Como** jugadores,  
**queremos** poder recolectar un Balón de Oro,  
**para** volvernos invencibles por un tiempo limitado.

### 🧃 Power-Up de reducción
**HU05**  
**Como** jugadores,  
**queremos** recoger una bebida especial,  
**para** que Messi se vuelva más pequeño temporalmente y sea más fácil esquivar obstáculos.

### 💥 Detección de colisiones
**HU06**  
**Como** jugadores,  
**queremos** que el juego detecte con precisión las colisiones,  
**para** que perder solo ocurra si realmente tocamos un obstáculo y no por pasar cerca.

### 📈 Sistema de puntuación
**HU07**  
**Como** jugadores,  
**queremos** ver el puntaje actual en pantalla,  
**para** saber cuánto hemos avanzado y motivarnos a mejorar.

### 🔁 Reinicio del juego
**HU08**  
**Como** jugadores,  
**queremos** poder reiniciar el juego al perder,  
**para** intentarlo nuevamente y superar nuestra puntuación anterior.

---

## 🛠️ Cómo ejecutar el juego

### Instrucciones
1. Cloná o descargá este repositorio.
2. Abrilo en tu IDE favorito.
3. Ejecutá `FlappyBird.java` como clase principal.
4. Presioná `Espacio` para iniciar el juego.
5. Usá la flecha `↑` para que Messi salte.
