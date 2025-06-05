package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface PowerUp {
    /** Actualiza la posición o estado interno del power-up */
    void update();

    /** Dibuja el power-up en pantalla según su estado */
    void draw(Graphics g);

    /** Aplica el efecto concreto sobre el pájaro (Bird) */
    void applyEffect(Bird bird);

    /** Retorna el área de colisión para detección de pickup */
    Rectangle getBounds();

    /** Indica si ya salió de la pantalla (para eliminarlo) */
    boolean isOffScreen();
}
