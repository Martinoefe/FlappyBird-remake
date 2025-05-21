package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GoldenBall implements PowerUp {
    private int x, y, size = 30;
    private boolean active = false;
    private long activationTime;
    private Image img;


    public GoldenBall(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            img = ImageIO.read(new File("images/balon_de_oro.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        x -= 3;
        if (active && System.currentTimeMillis() - activationTime > 5000) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(240); // 4 segundos
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isOffScreen() {
        return x + size <= 0;
    }
}
