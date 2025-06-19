package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class Defender
 * @brief Obstáculo móvil vertical que actúa como "defensor".
 *
 * Se desplaza hacia la izquierda y rebota entre límites superior e inferior.
 */
public class Defender {
    private float x, y;               ///< posición en pantalla
    private int speedY;               ///< velocidad vertical
    private int direction = 1;        ///< 1 = va hacia abajo, -1 = hacia arriba
    private Image img;                ///< imagen del defensor

    private static final int WIDTH = 50;                    ///< ancho del sprite
    private static final int HEIGHT = 50;                   ///< alto del sprite
    private static final int LIMIT_TOP = 10;                ///< límite superior en Y
    private static final int LIMIT_BOTTOM =                    ///< límite inferior en Y
            GameModel.HEIGHT - HEIGHT;

    /**
     * @brief Constructor.
     * @param startX posición X inicial (aparece fuera de pantalla a la derecha)
     */
    public Defender(int startX) {
        this.x = startX;
        this.y = (float)(Math.random() * (GameModel.HEIGHT - HEIGHT));
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/images/defender.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.speedY = 4 + (int)(Math.random() * 4);
    }

    /**
     * @brief Actualiza posición.
     * - Rebota en top/bottom invirtiendo dirección y a veces cambia velocidad.
     * - Se mueve 2 px a la izquierda.
     */
    public void updateState() {
        y += direction * speedY;
        if (y <= LIMIT_TOP) {
            y = LIMIT_TOP;
            direction = 1;
            ajustarVelocidadAleatoria();
        } else if (y >= LIMIT_BOTTOM) {
            y = LIMIT_BOTTOM;
            direction = -1;
            ajustarVelocidadAleatoria();
        }
        x -= 2;
    }

    /**
     * @brief Dibuja el defensor.
     * @param g contexto gráfico
     */
    public void draw(Graphics g) {
        g.drawImage(img, Math.round(x), Math.round(y), WIDTH, HEIGHT, null);
    }

    /**
     * @brief Obtiene área de colisión ajustada.
     * @return Rectangle con hitbox (ligeramente más estrecha)
     */
    public Rectangle getBounds() {
        return new Rectangle(
                Math.round(x + 5),
                Math.round(y),
                40, 50
        );
    }

    /**
     * @brief Cambia aleatoriamente la velocidad vertical (20% de probabilidad).
     */
    private void ajustarVelocidadAleatoria() {
        if (Math.random() < 0.2) {
            speedY = 4 + (int)(Math.random() * 4);
        }
    }

    /**
     * @brief Comprueba si salió de la pantalla por la izquierda.
     * @return true si ya no es visible
     */
    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }
}
