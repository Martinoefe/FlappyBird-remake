package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @interface PowerUp
 * @brief Interfaz para objetos que aplican un efecto al Bird.
 */
public interface PowerUp {
    /**
     * @brief Actualiza posición y estado interno del power-up.
     */
    void update();

    /**
     * @brief Dibuja el power-up en pantalla.
     * @param g contexto gráfico
     */
    void draw(Graphics g);

    /**
     * @brief Aplica el efecto al pájaro.
     * @param bird objeto Bird al cual aplicar el efecto
     */
    void applyEffect(Bird bird);

    /**
     * @brief Devuelve el área de colisión del power-up.
     * @return Rectángulo de colisión
     */
    Rectangle getBounds();

    /**
     * @brief Indica si el power-up ya salió de pantalla.
     * @return true si está off-screen
     */
    boolean isOffScreen();
}
