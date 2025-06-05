package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Power-up “MiniMessi”: al chocar con Bird, le aplica modo mini durante un tiempo determinado.
 * Se implementa como Singleton para que solo exista una única instancia.
 */
public class MiniMessi implements PowerUp {
    private int x, y;
    private final int size = 40;
    private Image img;

    // Instancia única (singleton)
    private static final MiniMessi instancia = new MiniMessi();

    /** Constructor privado */
    private MiniMessi() { }

    /**
     * Retorna la instancia única y la posiciona en (newX, newY), cargando la imagen.
     */
    public static MiniMessi getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(new File("images/bebida.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instancia;
    }

    /**
     * Se invoca cada frame: mueve la bebida hacia la izquierda.
     */
    @Override
    public void update() {
        x -= 3;
    }

    /**
     * Dibuja la bebida en pantalla.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * Al colisionar con Bird, invoca bird.makeMini(400) (5 s a 80 FPS).
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeMini(400);
    }

    /**
     * Hitbox cuadrada de 40×40.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * Indica si la bebida salió completamente de pantalla (x + size < 0).
     */
    @Override
    public boolean isOffScreen() {
        return x + size < 0;
    }
}
