package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Representa a un defensor que actúa como obstáculo móvil vertical en el juego.
 * Implementa la interfaz PowerUp, pero no aplica efectos positivos.
 * Se mueve verticalmente de arriba a abajo y viceversa, y también se desplaza hacia la izquierda.
 */
public class Defender implements PowerUp {

    private int x;                  // Posición horizontal del defensor
    private int y;                  // Posición vertical actual
    private int speedY;             // Velocidad vertical del defensor
    private int direction = 1;      // Dirección del movimiento vertical (1 = abajo, -1 = arriba)
    private Image image;           // Imagen del defensor

    private static final int width = 50;    // Ancho del defensor
    private static final int height = 50;   // Alto del defensor

    private static final int MOVE_LIMIT_TOP = 10;                // Límite superior de movimiento vertical
    private static final int MOVE_LIMIT_BOTTOM = 600 - height;   // Límite inferior (ajustable según alto del juego)
    private static final int SCREEN_WIDTH = 800;                 // Ancho de la pantalla del juego

    /**
     * Constructor que inicializa un defensor con posición X dada y posición Y aleatoria.
     * También le asigna una velocidad vertical aleatoria entre 4 y 7.
     * @param x posición inicial en el eje X
     */
    public Defender(int x) {
        this.x = x;
        this.y = (int) (Math.random() * 400); // posición Y inicial aleatoria
        this.image = new ImageIcon("images/defender.png").getImage();
        this.speedY = 4 + (int)(Math.random() * 4); // velocidad entre 4 y 7
    }

    /**
     * Actualiza la posición del defensor: se mueve verticalmente y rebota en los límites superior/inferior.
     * También se desplaza horizontalmente hacia la izquierda.
     */
    @Override
    public void update() {
        y += direction * speedY;

        // Rebote al llegar al límite superior
        if (y <= MOVE_LIMIT_TOP) {
            y = MOVE_LIMIT_TOP;
            direction *= -1;  // Cambia dirección
            ajustarVelocidadAleatoria(); // A veces cambia su velocidad
        }
        // Rebote al llegar al límite inferior
        else if (y + height >= MOVE_LIMIT_BOTTOM) {
            y = MOVE_LIMIT_BOTTOM - height;
            direction *= -1;
            ajustarVelocidadAleatoria();
        }

        x -= 2; // Movimiento horizontal hacia la izquierda
    }

    /**
     * Dibuja al defensor en la pantalla en su posición actual.
     * @param g el contexto gráfico donde se va a dibujar
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    /**
     * Indica que este objeto siempre está activo mientras esté en pantalla.
     * @return true
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * Devuelve el rectángulo de colisión del defensor, más ajustado que el tamaño real de la imagen.
     * Se utiliza para detectar colisiones con el personaje.
     * @return el rectángulo que representa el área de colisión del defensor
     */
    @Override
    public Rectangle getBounds() {
        int hitboxWidth = 40;
        int hitboxHeight = 50;
        return new Rectangle(
                x + (width - hitboxWidth) / 2,
                y + (height - hitboxHeight) / 2,
                hitboxWidth,
                hitboxHeight
        );
    }

    /**
     * Este "PowerUp" no tiene un efecto positivo. Las colisiones se manejan en la lógica principal del juego.
     * @param bird el pájaro con el que colisiona (sin efecto)
     */
    @Override
    public void applyEffect(Bird bird) {
        // No tiene efecto positivo, se maneja en FlappyBird al detectar colisión
    }

    /**
     * Determina si el defensor ya salió completamente de la pantalla por la izquierda.
     * @return true si está fuera de la pantalla, false si aún es visible
     */
    public boolean isOffScreen() {
        return x + width < 0;
    }

    /**
     * Devuelve la posición vertical del defensor (para control externo si se necesita).
     * @return la posición Y del defensor
     */
    public int getY() {
        return y;
    }

    /**
     * Ajusta aleatoriamente la velocidad vertical del defensor, solo con una probabilidad del 20%.
     */
    private void ajustarVelocidadAleatoria() {
        if (Math.random() < 0.2) {
            speedY = 4 + (int)(Math.random() * 4); // velocidad entre 4 y 7
        }
    }
}
