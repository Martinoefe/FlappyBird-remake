package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Representa un power-up de tipo "Balón de Oro" que otorga invencibilidad temporal al jugador.
 * Se implementa como Singleton para que solo exista una única instancia.
 */
public class GoldenBall implements PowerUp {

    private int x, y;                  // Posición del power-up
    private final int size = 30;       // Tamaño (ancho y alto)
    private boolean active = false;    // Indica si el efecto está en curso
    private long activationTime;       // Marca el instante en que se activó
    private Image img;                 // Imagen del Balón de Oro

    // Instancia única (singleton)
    private static final GoldenBall instancia = new GoldenBall();

    /** Constructor privado para forzar Singleton */
    private GoldenBall() { }

    /**
     * Retorna la instancia única y la posiciona en (newX, newY), cargando la imagen.
     */
    public static GoldenBall getInstancia(int newX, int newY) {
        instancia.x = newX;
        instancia.y = newY;
        try {
            instancia.img = ImageIO.read(new File("images/balon_de_oro.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        instancia.active = false;        // Reset del estado activo cada vez que se genera
        return instancia;
    }

    /**
     * Se invoca cada frame: mueve el power-up y apaga el efecto si ya pasaron 5 segundos.
     */
    @Override
    public void update() {
        x -= 3; // Movimiento horizontal hacia la izquierda

        // Si está activo y ya pasaron más de 5 segundos desde activationTime, desactivar
        if (active && System.currentTimeMillis() - activationTime > 5000) {
            active = false;
        }
    }

    /**
     * Dibuja la imagen del Balón de Oro en pantalla.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * Al colisionar con el pájaro, lo hace invencible durante 320 frames (~4 s a 80 FPS).
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(320);
        active = true;
        activationTime = System.currentTimeMillis();
    }

    /**
     * Hitbox del power-up (un rectángulo cuadrado de lado 'size').
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * Indica si el power-up ya salió completamente de la pantalla a la izquierda.
     */
    @Override
    public boolean isOffScreen() {
        return x + size <= 0;
    }
}
