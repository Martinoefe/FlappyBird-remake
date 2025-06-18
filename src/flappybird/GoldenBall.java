package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GoldenBall implements PowerUp {
    private int x, y;
    private final int size = 30;
    private Image img;

    private static final GoldenBall instancia = new GoldenBall();

    private GoldenBall() { }

    public static GoldenBall getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(GoldenBall.class.getResourceAsStream("/images/balon_de_oro.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instancia;
    }

    @Override
    public void update() {
        x -= 3;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(320);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    @Override
    public boolean isOffScreen() {
        return x + size <= 0;
    }
}