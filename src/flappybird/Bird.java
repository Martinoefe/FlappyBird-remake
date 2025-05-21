/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class Bird {
    public float x, y, vx, vy;
    public static final int RAD = 25;
    private Image img;
    private boolean invincible = false;
    private int invincibleTimer = 0; // duración en ticks

    public Bird() {
        x = FlappyBird.WIDTH/2;
        y = FlappyBird.HEIGHT/2;
        try {
            img = ImageIO.read(new File("images/messi.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void physics() {
        x += vx;
        y += vy;
        vy += 0.5f;

        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }
    }
    public void update(Graphics g) {
        g.drawImage(img, Math.round(x - RAD), Math.round(y - RAD), 2 * RAD, 2 * RAD, null);

        if (invincible) {   // Dibujar un círculo amarillo alrededor del pájaro si es invencible
            g.setColor(Color.YELLOW);
            g.drawOval(Math.round(x - RAD - 5), Math.round(y - RAD - 5), 2 * RAD + 10, 2 * RAD + 10);
        }
    }

    public void jump() {
        vy = -8;
    }
    
    public void reset() {
        x = 640/2;
        y = 640/2;
        vx = vy = 0;
    }

    public void makeInvincible(int duration) {
        invincible = true;
        invincibleTimer = duration;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
