package flappybird;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bird {
    public float x, y, vx, vy;
    public static final int WIDTH = 50;   // Tamaño normal
    public static final int HEIGHT = 50;  // Tamaño normal

    private Image img;
    private boolean invincible = false;
    private int invincibleTimer = 0;

    // ────────── PARA “Mini” ──────────
    private boolean mini = false;
    private static final float MINI_SCALE = 0.5f;
    private int miniTimer = 0;  // Cuenta regresiva (en frames) del modo mini

    public Bird() {
        x = FlappyBird.WIDTH / 2;
        y = FlappyBird.HEIGHT / 2;
        try {
            img = ImageIO.read(new File("images/messi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aplica la física básica: movimiento, gravedad e invencibilidad.
     * También decrementa el miniTimer y desactiva “mini” al terminar.
     */
    public void physics() {
        x += vx;
        y += vy;
        vy += 0.5f;

        // INVENCIBILIDAD
        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }

        // MODO MINI: descontar timer y, si llega a cero, restaurar tamaño
        if (mini) {
            miniTimer--;
            if (miniTimer <= 0) {
                mini = false;
            }
        }
    }

    /**
     * Dibuja al Bird. Si está mini, escala al 50%.
     */
    public void update(Graphics g) {
        int drawW = WIDTH;
        int drawH = HEIGHT;
        if (mini) {
            drawW = Math.round(WIDTH * MINI_SCALE);
            drawH = Math.round(HEIGHT * MINI_SCALE);
        }

        g.drawImage(img,
                Math.round(x - drawW / 2),
                Math.round(y - drawH / 2),
                drawW, drawH,
                null);

        if (invincible) {
            g.setColor(Color.YELLOW);
            g.drawOval(
                    Math.round(x - drawW / 2 - 5),
                    Math.round(y - drawH / 2 - 5),
                    drawW + 10,
                    drawH + 10
            );
        }
    }

    public void jump() {
        vy = -8;
    }

    /**
     * Reinicia la posición y estado al comenzal partida.
     * Incluye mini = false para asegurarse de que Bird vuelva a tamaño normal.
     */
    public void reset() {
        x = FlappyBird.WIDTH / 2;
        y = FlappyBird.HEIGHT / 2;
        vx = vy = 0;
        mini = false;
        invincible = false;
    }

    public void makeInvincible(int duration) {
        invincible = true;
        invincibleTimer = duration;
    }

    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Activa el modo “mini” por un número de frames (durationFrames).
     * Usaremos 400 frames (~5 segundos a 80 FPS).
     */
    public void makeMini(int durationFrames) {
        this.mini = true;
        this.miniTimer = durationFrames;
    }

    public boolean isMini() {
        return mini;
    }

    /**
     * Si está mini, devolvemos hitbox reducida; si no, hitbox normal.
     */
    public Rectangle getBounds() {
        if (mini) {
            int miniW = Math.round(WIDTH * MINI_SCALE);
            int miniH = Math.round(HEIGHT * MINI_SCALE);
            return new Rectangle(
                    (int) x - miniW / 2,
                    (int) y - miniH / 2,
                    miniW,
                    miniH
            );
        } else {
            int hitboxWidth = 25;
            int hitboxHeight = 35;
            return new Rectangle(
                    (int) x - hitboxWidth / 2,
                    (int) y - hitboxHeight / 2,
                    hitboxWidth,
                    hitboxHeight
            );
        }
    }
}
