package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class Pipe
 * @brief Representa una tubería (superior o inferior) en el juego.
 *
 * - Se desplaza hacia la izquierda.
 * - Dibuja su cuerpo y su cabeza.
 * - Proporciona su área de colisión y controla su expulsión de pantalla.
 */
public class Pipe {
    private float x;              ///< coordenada X horizontal
    private final int width;      ///< ancho de la tubería
    private final int height;     ///< alto de la tubería (variable)
    private final boolean top;    ///< true si es tubería superior
    private Image headImg;        ///< imagen de la cabeza
    private Image bodyImg;        ///< imagen del cuerpo

    /**
     * @brief Constructor completo.
     * @param startX posición X inicial
     * @param width ancho de la tubería
     * @param height alto de la tubería
     * @param top true para tubería superior
     * @param headImg imagen de la cabeza
     * @param bodyImg imagen del cuerpo
     */
    public Pipe(int startX, int width, int height, boolean top,
                Image headImg, Image bodyImg) {
        this.x = startX;
        this.width = width;
        this.height = height;
        this.top = top;
        this.headImg = headImg;
        this.bodyImg = bodyImg;
    }

    /**
     * @brief Constructor que carga imágenes por defecto.
     * @param startX posición X inicial
     * @param width ancho de la tubería
     * @param height alto de la tubería
     * @param top true para tubería superior
     */
    public Pipe(int startX, int width, int height, boolean top) {
        this(startX, width, height, top, null, null);
        try {
            this.headImg = ImageIO.read(
                    getClass().getResourceAsStream("/images/pipe.png"));
            this.bodyImg = ImageIO.read(
                    getClass().getResourceAsStream("/images/pipe_part.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Mueve la tubería a la izquierda.
     */
    public void updateState() {
        x -= 3;
    }

    /**
     * @brief Dibuja la tubería en pantalla.
     * @param g contexto gráfico
     */
    public void draw(Graphics g) {
        int drawY = top ? 0 : GameModel.HEIGHT - height;
        g.drawImage(bodyImg, Math.round(x), drawY, width, height, null);
        int headY = top ? height - 10 : GameModel.HEIGHT - height;
        g.drawImage(headImg, Math.round(x - 5), headY, width + 10, 20, null);
    }

    /**
     * @brief Obtiene el rectángulo de colisión.
     * @return Rectangle según posición y tamaño
     */
    public Rectangle getBounds() {
        int yPos = top ? 0 : GameModel.HEIGHT - height;
        return new Rectangle(Math.round(x), yPos, width, height);
    }

    /**
     * @brief Comprueba si salió de la pantalla.
     * @return true si ya no es visible por la izquierda
     */
    public boolean isOffScreen() {
        return x + width < 0;
    }

    /**
     * @brief Devuelve la coordenada X actual.
     * @return posición horizontal de la tubería
     */
    public float getX() {
        return x;
    }
}
