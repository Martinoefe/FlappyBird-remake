package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MiniMessi implements PowerUp {
    private int x, y;
    private final int size = 60;
    private Image img;

    private static final MiniMessi instancia = new MiniMessi();

    private MiniMessi() { }

    public static MiniMessi getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(MiniMessi.class.getResourceAsStream("/images/bebida.png"));
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
        bird.makeMini(400);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    @Override
    public boolean isOffScreen() {
        return x + size < 0;
    }
}