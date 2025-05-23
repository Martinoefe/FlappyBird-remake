package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Clase que representa el panel principal del juego. Se encarga de dibujar
 * todos los elementos en pantalla: fondo, pájaro, tuberías, power-ups, defensores y puntaje.
 */
public class GamePanel extends JPanel {

    private Bird bird;                      // Referencia al pájaro (jugador)
    private ArrayList<Pipe> pipes;          // Lista de tuberías en el juego
    private FlappyBird fb;                  // Referencia al controlador principal del juego

    private Font scoreFont, pauseFont;      // Fuentes para dibujar puntaje y mensaje de pausa

    private BufferedImage backgroundImg;    // Imagen de fondo del juego
    private int scrollX = 0;                // Control horizontal del scroll del fondo
    private static final int SCROLL_SPEED = 2; // Velocidad con la que se mueve el fondo

    // Constantes de tamaño de las tuberías
    public static final int PIPE_W = 50, PIPE_H = 30;

    /**
     * Constructor del panel del juego.
     * @param fb instancia del juego principal
     * @param bird instancia del pájaro
     * @param pipes lista de tuberías activas
     */
    public GamePanel(FlappyBird fb, Bird bird, ArrayList<Pipe> pipes) {
        this.fb = fb;
        this.bird = bird;
        this.pipes = pipes;

        // Carga de fuentes personalizadas
        scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);
        pauseFont = new Font("Arial", Font.BOLD, 48);

        // Carga de imagen de fondo
        try {
            backgroundImg = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Imprime error si no se encuentra la imagen
        }
    }

    /**
     * Metodo encargado de dibujar todos los elementos del juego en pantalla.
     * @param g objeto Graphics para dibujar
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Llama a la implementación base de JPanel

        // Dibuja el fondo del juego con efecto de desplazamiento horizontal
        if (backgroundImg != null) {
            int imgWidth = backgroundImg.getWidth();
            scrollX -= SCROLL_SPEED;

            // Si el fondo ha desplazado completamente, lo reinicia
            if (scrollX <= -imgWidth) {
                scrollX = 0;
            }

            // Repite el fondo varias veces horizontalmente para dar efecto de desplazamiento infinito
            for (int x = scrollX; x < FlappyBird.WIDTH; x += imgWidth) {
                g.drawImage(backgroundImg, x, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT, null);
            }
        } else {
            // Si la imagen no carga, dibuja un fondo de color
            g.setColor(new Color(0, 158, 158));
            g.fillRect(0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
        }

        // Dibuja el pájaro
        bird.update(g);

        // Dibuja todos los power-ups
        for (PowerUp p : fb.getPowerUps()) {
            p.draw(g);
        }

        // Dibuja todas las tuberías
        for (Pipe p : pipes) {
            p.draw(g);
        }

        // Dibuja todos los defensores
        for (Defender d : fb.getDefenders()) {
            d.draw(g);
        }

        // Dibuja el puntaje en la esquina superior izquierda
        g.setFont(scoreFont);
        g.setColor(Color.white);
        g.drawString("Score: " + fb.getScore(), 10, 20);

        // Si el juego está pausado, muestra mensaje en pantalla
        if (fb.paused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(255, 255, 255, 200)); // Blanco con transparencia
            g.drawString("PAUSED", FlappyBird.WIDTH / 2 - 100, FlappyBird.HEIGHT / 2 - 100);
            g.drawString("PRESS SPACE TO BEGIN", FlappyBird.WIDTH / 2 - 300, FlappyBird.HEIGHT / 2 + 50);
        }
    }
}
