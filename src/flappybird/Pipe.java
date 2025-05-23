package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Representa una tubería en el juego, que puede ser superior o inferior.
 * Se compone de dos imágenes: una para la parte larga (cuerpo) y otra para la parte superior (cabeza).
 * Se mueve hacia la izquierda constantemente simulando desplazamiento.
 */
public class Pipe {

    private int x;             // Posición horizontal de la tubería
    private int width;         // Ancho de la tubería
    private int height;        // Alto de la tubería
    private boolean top;       // Indica si la tubería es superior (true) o inferior (false)
    private Image head;        // Imagen de la "cabeza" decorativa de la tubería
    private Image length;      // Imagen del "cuerpo" de la tubería (parte larga)

    /**
     * Constructor que inicializa una tubería con sus atributos gráficos y de posición.
     * @param x posición inicial en el eje X
     * @param width ancho de la tubería
     * @param height alto de la tubería
     * @param top true si es una tubería superior, false si es inferior
     * @param head imagen de la cabeza de la tubería
     * @param length imagen del cuerpo de la tubería
     */
    public Pipe(int x, int width, int height, boolean top, Image head, Image length) {
        this.x = x;
        this.width = width;
        this.height = height;
        this.top = top;
        this.head = head;
        this.length = length;
    }

    /**
     * Dibuja la tubería en pantalla usando las imágenes proporcionadas.
     * La posición Y depende de si es una tubería superior o inferior.
     * @param g contexto gráfico sobre el que se dibuja
     */
    public void draw(Graphics g) {
        // Dibuja el cuerpo de la tubería
        g.drawImage(length, x, top ? 0 : FlappyBird.HEIGHT - height, width, height, null);

        // Dibuja la cabeza de la tubería
        g.drawImage(head, x - 5, top ? height - 10 : FlappyBird.HEIGHT - height, width + 10, 20, null);
    }

    /**
     * Actualiza la posición de la tubería desplazándola hacia la izquierda.
     */
    public void update() {
        x -= 3;
    }

    /**
     * Verifica si la tubería ya salió completamente de la pantalla.
     * @return true si la tubería está fuera del área visible, false en caso contrario
     */
    public boolean isOffScreen() {
        return x + width < 0;
    }

    /**
     * Devuelve la posición horizontal actual de la tubería.
     * @return coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve el ancho de la tubería.
     * @return ancho en píxeles
     */
    public int getWidth() {
        return width;
    }

    /**
     * Devuelve un rectángulo que representa el área de colisión de la tubería.
     * @return el rectángulo de colisión
     */
    public Rectangle getBounds() {
        if (top) {
            return new Rectangle(x, 0, width, height);
        } else {
            return new Rectangle(x, FlappyBird.HEIGHT - height, width, height);
        }
    }

    /**
     * Devuelve la altura de la tubería.
     * @return altura en píxeles
     */
    public int getHeight() {
        return height;
    }
}
