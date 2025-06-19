package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @interface PowerUp
 * @brief Interfaz para todos los objetos recuperables que otorgan efectos al Bird.
 *
 * Define métodos para la actualización, dibujo, aplicación de efecto,
 * obtención de colisión y verificación de salida de pantalla.
 */
public interface PowerUp {
    /**
     * @brief Actualiza posición o estado interno del power‑up.
     */
    void update();

    /**
     * @brief Dibuja el power‑up en pantalla.
     * @param g contexto gráfico
     */
    void draw(Graphics g);

    /**
     * @brief Aplica el efecto concreto sobre el Bird.
     * @param bird instancia de Bird que recibe el efecto
     */
    void applyEffect(Bird bird);

    /**
     * @brief Área de colisión para detección de recogida.
     * @return Rectangle con la hitbox actual
     */
    Rectangle getBounds();

    /**
     * @brief Indica si ya salió de la pantalla para descartarlo.
     * @return true si está completamente fuera de la ventana
     */
    boolean isOffScreen();
}
