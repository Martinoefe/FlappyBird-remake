package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class GoldenBall
 * @brief Power‑up que hace al pájaro invencible durante un número de frames.
 */
public class GoldenBall implements PowerUp {
    private int x, y;           ///< Posición actual de la bola dorada
    private final int size = 30;///< Tamaño (ancho = alto) de la bola

    private Image img;          ///< Imagen que se dibuja

    /// Instancia singleton
    private static final GoldenBall instancia = new GoldenBall();

    private GoldenBall() { }

    /**
     * @brief Obtiene la instancia única, reposicionándola.
     * @param newX Nueva coordenada X
     * @param newY Nueva coordenada Y
     * @return la instancia singleton de GoldenBall
     */
    public static GoldenBall getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(
                    GoldenBall.class.getResourceAsStream("/images/balon_de_oro.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instancia;
    }

    /**
     * @brief Mueve la bola hacia la izquierda cada frame.
     */
    @Override
    public void update() {
        x -= 3;
    }

    /**
     * @brief Dibuja la bola dorada en pantalla.
     * @param g Contexto gráfico donde pintar
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * @brief Aplica el efecto de invencibilidad al pájaro.
     * @param bird Instancia del pájaro a afectar
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(320);
    }

    /**
     * @brief Obtiene el rectángulo de colisión de la bola.
     * @return Un Rectangle de tamaño `size` en `(x,y)`
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * @brief Indica si la bola ha salido de la pantalla por la izquierda.
     * @return `true` si `x + size <= 0`
     */
    @Override
    public boolean isOffScreen() {
        return x + size <= 0;
    }
}
