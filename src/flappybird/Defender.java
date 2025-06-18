package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Defender {
    private float x, y;
    private int speedY;
    private int direction = 1;
    private Image img;

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int LIMIT_TOP = 10;
    private static final int LIMIT_BOTTOM = GameModel.HEIGHT - HEIGHT;

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

    public void draw(Graphics g) {
        g.drawImage(img, Math.round(x), Math.round(y), WIDTH, HEIGHT, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x + 5), Math.round(y), 40, 50);
    }

    private void ajustarVelocidadAleatoria() {
        if (Math.random() < 0.2) {
            speedY = 4 + (int)(Math.random() * 4);
        }
    }

    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }
}