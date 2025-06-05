package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Defender {
    private float x, y;
    private int speedY;
    private int direction = 1; // 1 = bajando, -1 = subiendo
    private Image img;

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int LIMIT_TOP = 10;
    private static final int LIMIT_BOTTOM = GameModel.HEIGHT - HEIGHT;

    public Defender(int startX) {
        this.x = startX;
        this.y = (float)(Math.random() * (GameModel.HEIGHT - HEIGHT));
        try {
            img = ImageIO.read(new File("images/defender.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.speedY = 4 + (int)(Math.random() * 4); // entre 4 y 7
    }

    /** Mueve hacia la izquierda y rebota verticalmente */
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

    /** Dibuja el defensor en pantalla */
    public void draw(Graphics g) {
        g.drawImage(img, Math.round(x), Math.round(y), WIDTH, HEIGHT, null);
    }

    /** Hitbox más ajustada que la imagen completa */
    public Rectangle getBounds() {
        int hitW = 40, hitH = 50;
        return new Rectangle(
            Math.round(x + (WIDTH - hitW)/2f),
            Math.round(y + (HEIGHT - hitH)/2f),
            hitW, hitH
        );
    }

    /** Cambia la velocidad vertical al azar con 20% de probabilidad */
    private void ajustarVelocidadAleatoria() {
        if (Math.random() < 0.2) {
            speedY = 4 + (int)(Math.random() * 4);
        }
    }

    /** @return true si ya salió completamente de la pantalla (x + ancho < 0) */
    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }
}
