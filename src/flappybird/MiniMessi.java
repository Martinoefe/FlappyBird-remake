package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class MiniMessi
 * @brief Power‑up que reduce temporalmente el tamaño del pájaro.
 */
public class MiniMessi implements PowerUp {
    private int       x, y;      ///< Posición actual del power‑up
    private final int size = 60; ///< Tamaño original del sprite

    private Image img; ///< Imagen del power‑up

    /// Instancia singleton
    private static final MiniMessi instancia = new MiniMessi();

    private MiniMessi() {}

    /**
     * @brief Obtiene la instancia única, reposicionándola.
     * @param newX Nueva coordenada X
     * @param newY Nueva coordenada Y
     * @return la instancia singleton de MiniMessi
     */
    public static MiniMessi getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(MiniMessi.class.getResourceAsStream("/images/bebida.png"));
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return instancia;
    }

    /**
     * @brief Desplaza el power‑up hacia la izquierda cada frame.
     */
    @Override
    public void update() {
        x -= 3;
    }

    /**
     * @brief Dibuja el power‑up en la posición actual.
     * @param g Contexto gráfico donde pintar
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * @brief Aplica el efecto de miniatura al pájaro.
     * @param bird Instancia del pájaro a afectar
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeMini(400);
    }

    /**
     * @brief Devuelve el rectángulo de colisión del power‑up.
     * @return Un Rectangle de tamaño `size` en `(x,y)`
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * @brief Comprueba si el power‑up ha salido de la pantalla.
     * @return `true` si `x + size < 0`
     */
    @Override
    public boolean isOffScreen() {
        return x + size < 0;
    }
}
