package flappybird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Pipe {
    int x, y, width, height;
    boolean isTop;
    Image head, length;

    public Pipe(int x, int width, int height, boolean isTop, Image head, Image length) {
        this.x = x;
        this.y = isTop ? 0 : FlappyBird.HEIGHT - height;
        this.width = width;
        this.height = height;
        this.isTop = isTop;
        this.head = head;
        this.length = length;
    }

    public void update() {
        x -= 3;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        g2d.translate(x + width / 2, y + GamePanel.PIPE_H / 2);
        if (isTop) {
            g2d.translate(0, height);
            g2d.rotate(Math.PI);
        }

        g2d.drawImage(head, -width / 2, -GamePanel.PIPE_H / 2, width, GamePanel.PIPE_H, null);
        g2d.drawImage(length, -width / 2, GamePanel.PIPE_H / 2, width, height, null);
        g2d.setTransform(old);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOffScreen() {
        return x + width <= 0;
    }
}
