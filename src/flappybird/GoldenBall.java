package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Representa un power-up de tipo "Balón de Oro" que otorga invencibilidad temporal al jugador.
 * Se mueve horizontalmente hacia la izquierda y desaparece una vez recogido o al salir de pantalla.
 */
public class GoldenBall implements PowerUp {

    private int x, y;                  // Posición del power-up
    private final int size = 30;      // Tamaño (ancho y alto)
    private boolean active = false;   // Indica si está activo tras colisión
    private long activationTime;      // Tiempo en que se activó el efecto
    private Image img;                // Imagen del Balón de Oro

    /**
     * Constructor que crea el power-up en una posición determinada.
     * @param x posición inicial en el eje X
     * @param y posición inicial en el eje Y
     */
    public GoldenBall(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            img = ImageIO.read(new File("images/balon_de_oro.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la posición del power-up y controla el tiempo de efecto si está activo.
     * Se mueve hacia la izquierda de forma constante.
     */
    @Override
    public void update() {
        x -= 3; // Movimiento horizontal hacia la izquierda

        // Desactiva el efecto si han pasado más de 5 segundos
        if (active && System.currentTimeMillis() - activationTime > 5000) {
            active = false;
        }
    }

    /**
     * Dibuja la imagen del Balón de Oro en pantalla.
     * @param g el contexto gráfico donde se dibuja
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }

    /**
     * Aplica el efecto al pájaro (invencibilidad por 4 segundos) y marca como activo.
     * @param bird el objeto Bird al que se le aplica el efecto
     */
    @Override
    public void applyEffect(Bird bird) {
        bird.makeInvincible(320); // Invencibilidad durante 320 frames (~4 segundos a 80fps)
        active = true;
        activationTime = System.currentTimeMillis();
    }

    /**
     * Indica si el efecto del power-up sigue activo.
     * @return true si está activo, false si ya expiró
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Devuelve el área de colisión del power-up.
     * @return un objeto Rectangle que representa su hitbox
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * Verifica si el power-up ya salió completamente de la pantalla por la izquierda.
     * @return true si ya no es visible, false en caso contrario
     */
    public boolean isOffScreen() {
        return x + size <= 0;
    }

    /**
     * Devuelve la posición horizontal actual del power-up.
     * @return la coordenada X del Balón de Oro
     */
    public int getX() {
        return x;
    }
}
