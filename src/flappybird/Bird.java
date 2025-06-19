package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @class Bird
 * @brief Controla la lógica y dibujo del jugador (el pájaro).
 *
 * Gestiona posición, física (gravedad y salto), modos de invencibilidad y "mini",
 * y su representación gráfica.
 */
public class Bird {
    float x, y, vx, vy;              ///< posición y velocidad en X/Y
    public static final int WIDTH = 50;  ///< ancho de la imagen
    public static final int HEIGHT = 50; ///< alto de la imagen

    private Image img;                ///< imagen cargada del pájaro
    private boolean invincible = false;///< estado de invencibilidad
    private int invincibleTimer = 0;  ///< frames restantes de invencibilidad

    private boolean mini = false;     ///< estado de modo mini
    private int miniTimer = 0;        ///< frames restantes en modo mini
    private static final float MINI_SCALE = 0.5f; ///< factor de escala en modo mini

    /**
     * @brief Constructor por defecto.
     * Coloca al pájaro en el centro de la pantalla y carga su imagen.
     */
    public Bird() {
        x = GameModel.WIDTH / 2f;
        y = GameModel.HEIGHT / 2f;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/images/messi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Actualiza física y temporizadores.
     * - Aplica velocidad y gravedad.
     * - Decrementa los contadores de invencibilidad y mini.
     */
    public void updateState() {
        x += vx;
        y += vy;
        vy += 0.5f;
        if (invincible) {
            if (--invincibleTimer <= 0) invincible = false;
        }
        if (mini) {
            if (--miniTimer <= 0) mini = false;
        }
    }

    /**
     * @brief Dibuja el pájaro en pantalla.
     * Si está en modo mini escala la imagen, si está invencible dibuja un óvalo amarillo.
     * @param g contexto gráfico donde dibujar
     */
    public void draw(Graphics g) {
        int drawW = mini ? Math.round(WIDTH * MINI_SCALE) : WIDTH;
        int drawH = mini ? Math.round(HEIGHT * MINI_SCALE) : HEIGHT;
        g.drawImage(img,
                Math.round(x - drawW/2),
                Math.round(y - drawH/2),
                drawW, drawH,
                null);
        if (invincible) {
            g.setColor(Color.YELLOW);
            g.drawOval(Math.round(x - drawW/2 - 5),
                    Math.round(y - drawH/2 - 5),
                    drawW + 10, drawH + 10);
        }
    }

    /**
     * @brief Provoca un salto instantáneo negativo en la velocidad vertical.
     */
    public void jump() {
        vy = -8;
    }

    /**
     * @brief Activa invencibilidad durante un número de frames.
     * @param durationFrames duración en frames de la invencibilidad
     */
    public void makeInvincible(int durationFrames) {
        invincible = true;
        invincibleTimer = durationFrames;
    }

    /**
     * @brief Activa el modo mini durante un número de frames.
     * @param durationFrames duración en frames del modo mini
     */
    public void makeMini(int durationFrames) {
        mini = true;
        miniTimer = durationFrames;
    }

    /** @return true si está en modo invencible */
    public boolean isInvincible() { return invincible; }

    /** @return true si está en modo mini */
    public boolean isMini()       { return mini; }

    /**
     * @brief Obtiene el rectángulo de colisión.
     * El tamaño varía si está en modo mini.
     * @return Rectangle con la hitbox actual
     */
    public Rectangle getBounds() {
        int w = 25, h = 35;
        if (mini) {
            w = Math.round(w * MINI_SCALE);
            h = Math.round(h * MINI_SCALE);
        }
        return new Rectangle(
                Math.round(x - w/2),
                Math.round(y - h/2),
                w, h
        );
    }

    /**
     * @brief Devuelve la coordenada y actual.
     * @return posición vertical del pájaro
     */
    public float getY() { return y; }

    /**
     * @brief Reinicia al pájaro a la posición y estado inicial.
     */
    public void reset() {
        x = GameModel.WIDTH / 2f;
        y = GameModel.HEIGHT / 2f;
        vx = vy = 0;
        invincible = mini = false;
    }
}
