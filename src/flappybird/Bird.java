package flappybird;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que representa al personaje principal del juego (Messi).
 * Controla su posición, física (gravedad, salto), dibujo y estado de invencibilidad.
 */
public class Bird {

    public float x, y, vx, vy; // Posiciones y velocidades del pájaro en el eje X e Y
    public static final int WIDTH = 50;  // Ancho de la imagen del pájaro
    public static final int HEIGHT = 50; // Altura de la imagen del pájaro

    private Image img;                   // Imagen del personaje (Messi)
    private boolean invincible = false; // Indica si el personaje está temporalmente invencible
    private int invincibleTimer = 0;    // Duración restante del estado de invencibilidad (en frames)

    /**
     * Constructor que inicializa la posición del pájaro en el centro de la pantalla y carga la imagen.
     */
    public Bird() {
        x = FlappyBird.WIDTH / 2;
        y = FlappyBird.HEIGHT / 2;
        try {
            img = ImageIO.read(new File("images/messi.png")); // Carga la imagen del personaje
        } catch (IOException e) {
            e.printStackTrace(); // Imprime error si no se puede cargar la imagen
        }
    }

    /**
     * Aplica la física básica al pájaro: movimiento y gravedad.
     * También reduce el temporizador de invencibilidad si está activo.
     */
    public void physics() {
        x += vx;         // Aplica velocidad horizontal (normalmente cero)
        y += vy;         // Aplica velocidad vertical
        vy += 0.5f;      // Aplica gravedad

        // Manejo de invencibilidad
        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }
    }

    /**
     * Dibuja al pájaro en pantalla. Si está invencible, dibuja un contorno amarillo alrededor.
     * @param g el objeto Graphics sobre el cual se dibuja
     */
    public void update(Graphics g) {
        g.drawImage(img, Math.round(x - WIDTH / 2), Math.round(y - HEIGHT / 2), WIDTH, HEIGHT, null);

        // Dibuja una circunferencia amarilla para indicar invencibilidad
        if (invincible) {
            g.setColor(Color.YELLOW);
            g.drawOval(Math.round(x - WIDTH / 2 - 5), Math.round(y - HEIGHT / 2 - 5), WIDTH + 10, HEIGHT + 10);
        }
    }

    /**
     * Simula el salto del pájaro aplicando una velocidad vertical negativa.
     */
    public void jump() {
        vy = -8;
    }

    /**
     * Reinicia la posición y velocidad del pájaro. Se llama cuando comienza una nueva partida.
     */
    public void reset() {
        x = FlappyBird.WIDTH / 2;
        y = FlappyBird.HEIGHT / 2;
        vx = vy = 0;
    }

    /**
     * Activa el estado de invencibilidad durante cierta cantidad de frames.
     * @param duration duración de la invencibilidad
     */
    public void makeInvincible(int duration) {
        invincible = true;
        invincibleTimer = duration;
    }

    /**
     * Retorna si el pájaro está actualmente en estado de invencibilidad.
     * @return true si es invencible, false en caso contrario
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Retorna el área de colisión (hitbox) del pájaro.
     * Se usa para detectar colisiones con obstáculos o power-ups.
     * @return un rectángulo que representa el hitbox del pájaro
     */
    public Rectangle getBounds() {
        int hitboxWidth = 25;  // Ancho del hitbox
        int hitboxHeight = 35; // Altura del hitbox
        return new Rectangle((int) x - hitboxWidth / 2, (int) y - hitboxHeight / 2, hitboxWidth, hitboxHeight);
    }
}
