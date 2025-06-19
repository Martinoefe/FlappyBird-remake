package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class GoldenBall
 * @brief PowerUp único (singleton) que otorga invencibilidad temporal.
 *
 * - Se mueve hacia la izquierda.
 * - Aplica invencibilidad al colisionar.
 * - Se instancia una única vez.
 */
public class GoldenBall implements PowerUp {
    private int x, y;                 ///< posición del PowerUp
    private final int size = 30;      ///< tamaño del sprite
    private Image img;                ///< imagen del balón de oro

    private static final GoldenBall instancia = new GoldenBall(); ///< singleton

    private GoldenBall() { }

    /**
     * @brief Obtiene la instancia y sitúa el PowerUp.
     * @param newX coordenada X inicial
     * @param newY coordenada Y inicial
     * @return instancia única de GoldenBall
     */
    public static GoldenBall getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(
                    GoldenBall.class.getResourceAsStream("/images/balon_de_oro.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instancia;
    }

    /** @copydoc PowerUp#update() */
    @Override
    public void update() {
        x -= 3;
    }

    /** @copydoc PowerUp#draw(Graphics) */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /** @copydoc PowerUp#applyEffect(Bird) */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(320);
    }

    /** @copydoc PowerUp#getBounds() */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /** @copydoc PowerUp#isOffScreen() */
    @Override
    public boolean isOffScreen() {
        return x + size <= 0;
    }
}
