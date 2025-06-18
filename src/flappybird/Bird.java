package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bird {
    private float x, y, vx, vy;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;

    private Image img;
    private boolean invincible = false;
    private int invincibleTimer = 0;

    private boolean mini = false;
    private int miniTimer = 0;
    private static final float MINI_SCALE = 0.5f;

    public Bird() {
        x = GameModel.WIDTH / 2f;
        y = GameModel.HEIGHT / 2f;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/images/messi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateState() {
        x += vx;
        y += vy;
        vy += 0.5f;
        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) invincible = false;
        }
        if (mini) {
            miniTimer--;
            if (miniTimer <= 0) mini = false;
        }
    }

    public void draw(Graphics g) {
        int drawW = WIDTH;
        int drawH = HEIGHT;
        if (mini) {
            drawW = Math.round(WIDTH * MINI_SCALE);
            drawH = Math.round(HEIGHT * MINI_SCALE);
        }
        g.drawImage(img, Math.round(x - drawW / 2), Math.round(y - drawH / 2), drawW, drawH, null);
        if (invincible) {
            g.setColor(Color.YELLOW);
            g.drawOval(Math.round(x - drawW / 2 - 5), Math.round(y - drawH / 2 - 5), drawW + 10, drawH + 10);
        }
    }

    public void jump() {
        vy = -8;
    }

    public void makeInvincible(int durationFrames) {
        invincible = true;
        invincibleTimer = durationFrames;
    }

    public void makeMini(int durationFrames) {
        mini = true;
        miniTimer = durationFrames;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isMini() {
        return mini;
    }

    public Rectangle getBounds() {
        int hitboxW = 25, hitboxH = 35;
        if (mini) {
            int w = Math.round(hitboxW * MINI_SCALE);
            int h = Math.round(hitboxH * MINI_SCALE);
            return new Rectangle(Math.round(x - w / 2), Math.round(y - h / 2), w, h);
        } else {
            return new Rectangle(Math.round(x - hitboxW / 2), Math.round(y - hitboxH / 2), hitboxW, hitboxH);
        }
    }

    public float getY() {
        return y;
    }

    public void reset() {
        x = GameModel.WIDTH / 2f;
        y = GameModel.HEIGHT / 2f;
        vx = vy = 0;
        invincible = false;
        mini = false;
    }
}