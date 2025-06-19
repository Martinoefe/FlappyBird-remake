package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @interface PowerUp
 * @brief Interfaz para efectos que aplican al pájaro.
 */
public interface PowerUp {
    /**
     * @brief Actualiza la posición y lógica interna.
     */
    void update();

    /**
     * @brief Dibuja el power‑up en pantalla.
     * @param g contexto gráfico
     */
    void draw(Graphics g);

    /**
     * @brief Aplica el efecto al pájaro.
     * @param bird instancia del pájaro
     */
    void applyEffect(Bird bird);

    /**
     * @brief Obtiene su área de colisión.
     * @return rectángulo de colisión
     */
    Rectangle getBounds();

    /**
     * @brief Indica si ya salió de pantalla.
     * @return true si está off‑screen
     */
    boolean isOffScreen();
}
