package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Power-up “MiniMessi”: al chocar con Bird, le aplica modo mini durante un tiempo determinado.
 * Implementa PowerUp (patrón Strategy).
 */
public class MiniMessi implements PowerUp {
    private int x, y;
    private final int size = 40;
    private Image img;

    public MiniMessi(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        try {
            img = ImageIO.read(new File("images/bebida.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se mueve hacia la izquierda. Como ya no necesitamos gestionar duración aquí,
     * solo movemos la bebida.
     */
    @Override
    public void update() {
        x -= 3;
    }

    /**
     * Dibuja la bebida si no ha sido recogida aún.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * En el momento en que Bird colisiona con la bebida:
     * - Disparamos bird.makeMini(400) para 5 seg (400 frames a 80 FPS).
     * - No mostramos ningún JOptionPane.
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeMini(400);
    }

    @Override
    public boolean isActive() {
        // No se usa un flag “active” aquí; FlappyBird remueve el objeto apenas lo recoge.
        return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

   
    /**
     * Indica si la bebida ya salió de la pantalla (para eliminarla).
     */
      @Override
    public boolean isOffScreen() {
        return x + size < 0;
    }
}
