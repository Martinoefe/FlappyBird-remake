package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class MiniMessi
 * @brief PowerUp único (singleton) que reduce el tamaño del pájaro temporalmente.
 *
 * - Se mueve hacia la izquierda.
 * - Al colisionar, hace al pájaro “mini” durante un tiempo.
 * - Instancia única.
 */
public class MiniMessi implements PowerUp {
    private int x, y;               ///< posición del PowerUp
    private final int size = 60;    ///< tamaño del sprite ampliado
    private Image img;              ///< imagen de la bebida

    private static final MiniMessi instancia = new MiniMessi(); ///< singleton

    private MiniMessi() { }

    /**
     * @brief Obtiene la instancia y la reposiciona.
     * @param newX coordenada X de spawn
     * @param newY coordenada Y de spawn
     * @return instancia única de MiniMessi
     */
    public static MiniMessi getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(
                    MiniMessi.class.getResourceAsStream("/images/bebida.png"));
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
        bird.makeMini(400);
    }

    /** @copydoc PowerUp#getBounds() */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /** @copydoc PowerUp#isOffScreen() */
    @Override
    public boolean isOffScreen() {
        return x + size < 0;
    }
}
