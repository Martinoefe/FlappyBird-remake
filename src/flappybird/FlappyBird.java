package flappybird;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Clase principal del juego Flappy Bird.
 * Controla la lógica del juego, generación de obstáculos y power-ups, físicas del pájaro,
 * y colisiones con objetos.
 */
public class FlappyBird implements ActionListener, KeyListener {

    // Constantes generales
    public static final int FPS = 80, WIDTH = 800, HEIGHT = 600;

    // Componentes y variables del juego
    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Pipe> pipes;
    private int time, scroll; // tiempo transcurrido (puntaje), desplazamiento lateral
    private Timer t;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Defender> defenders = new ArrayList<>();
    private long lastDefenderSpawnTime = System.currentTimeMillis(); // para control de aparicion de defensores
    private boolean paused;

    // Imágenes para los tubos
    private Image pipeHead, pipeLength;

    /**
     * Inicializa y lanza la ventana del juego.
     */
    public void go() {
        frame = new JFrame("Flappy Bird");
        bird = new Bird();
        pipes = new ArrayList<>();
        powerUps = new ArrayList<>();

        // Cargar imágenes de los tubos
        try {
            pipeHead = ImageIO.read(new File("images/pipe.png"));
            pipeLength = ImageIO.read(new File("images/pipe_part.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear panel principal con renderizado personalizado
        panel = new GamePanel(this, bird, pipes);
        frame.add(panel);

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this); // escucha teclado

        paused = true; // el juego comienza pausado

        // Timer que actualiza el juego 80 veces por segundo
        t = new Timer(1000 / FPS, this);
        t.start();
    }

    /**
     * Metodo principal: lanza el juego.
     */
    public static void main(String[] args) {
        new FlappyBird().go();
    }

    /**
     * Lógica ejecutada en cada frame del juego (80 veces por segundo).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();

        if (!paused) {
            bird.physics();
            generarTuberias();
            generarGoldenBall();
            generarDefensor();
            actualizarTuberiasYDetectarColisiones();
            actualizarPowerUps();
            actualizarDefensores();
            verificarCaidaDelPajaro();
            time++;
            scroll++;
        }
    }

    /**
     * Genera nuevas tuberías cada 90 frames.
     * Las tuberías se generan con alturas aleatorias dentro de un rango.
     */
    private void generarTuberias() {
        if (scroll % 90 == 0) {
            int h1 = (int) ((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            int h2 = (int) ((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true, pipeHead, pipeLength));
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h2, false, pipeHead, pipeLength));
        }
    }

    /**
     * Genera un power-up GoldenBall cada 700 frames.
     * Se asegura de que no se genere en una posición que colisione con tuberías existentes.
     */
    private void generarGoldenBall() {
        if (scroll % 700 == 0) {
            int yPowerUp;
            boolean validPosition;
            int attempts = 0;

            do {
                yPowerUp = (int) (Math.random() * (HEIGHT - 100));
                validPosition = true;
                Rectangle ballBounds = new Rectangle(WIDTH, yPowerUp, 40, 40);
                for (Pipe pipe : pipes) {
                    if (pipe.getBounds().intersects(ballBounds)) {
                        validPosition = false;
                        break;
                    }
                }
                attempts++;
            } while (!validPosition && attempts < 10);

            powerUps.add(new GoldenBall(WIDTH, yPowerUp));
        }
    }

    /**
     * Genera un defensor cada 8000 ms (8 segundos) si no hay superposición con tuberías.
     * Los defensores se generan en una posición fija a la derecha de la pantalla.
     */
    private void generarDefensor() {
        if (System.currentTimeMillis() - lastDefenderSpawnTime >= 8000) {
            int defenderX = WIDTH + 100;
            boolean overlapsPipe = false;
            for (Pipe pipe : pipes) {
                if (Math.abs(pipe.getX() - defenderX) < 100) {
                    overlapsPipe = true;
                    break;
                }
            }

            if (!overlapsPipe) {
                defenders.add(new Defender(defenderX));
                lastDefenderSpawnTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Actualiza las tuberías, detecta colisiones con el pájaro y elimina las que están fuera de pantalla.
     * Si hay colisión, muestra un mensaje y reinicia el juego.
     */
    private void actualizarTuberiasYDetectarColisiones() {
        ArrayList<Pipe> toRemove = new ArrayList<>();
        for (Pipe p : pipes) {
            p.update();
            if (p.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                JOptionPane.showMessageDialog(frame, "Te chocaste con una tubería.\nTu puntaje fue: " + time);
                paused = true;
                resetGame();
                return;
            }
            if (p.isOffScreen()) {
                toRemove.add(p);
            }
        }
        pipes.removeAll(toRemove);
    }

    /**
     * Actualiza los power-ups, aplica sus efectos al pájaro si hay colisión,
     * y elimina los que están fuera de pantalla.
     */
    private void actualizarPowerUps() {
        ArrayList<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp p : powerUps) {
            p.update();
            if (p.getBounds().intersects(bird.getBounds())) {
                p.applyEffect(bird);
                toRemove.add(p);
            }
            if (p instanceof GoldenBall && ((GoldenBall) p).isOffScreen()) {
                toRemove.add(p);
            }
        }
        powerUps.removeAll(toRemove);
    }

    /**
     * Actualiza los defensores, verifica colisiones con el pájaro,
     * y elimina los que están fuera de pantalla.
     */
    private void actualizarDefensores() {
        Iterator<Defender> it = defenders.iterator();
        while (it.hasNext()) {
            Defender d = it.next();
            d.update();
            if (d.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                JOptionPane.showMessageDialog(frame, "Te chocaste con un defensor.\nTu puntaje fue: " + time);
                paused = true;
                resetGame();
                return;
            }
            if (d.isOffScreen()) {
                it.remove();
            }
        }
    }

    /**
     * Verifica si el pájaro ha caído fuera de los límites del juego.
     * Si cae, muestra un mensaje con el puntaje y reinicia el juego.
     */
    private void verificarCaidaDelPajaro() {
        if (bird.y > HEIGHT || bird.y + Bird.HEIGHT / 2 < 0) {
            JOptionPane.showMessageDialog(frame, "Te caíste.\nTu puntaje fue: " + time);
            paused = true;
            resetGame();
        }
    }

    /**
     * Devuelve el puntaje actual.
     */
    public int getScore() {
        return time;
    }

    /**
     * Control de teclas presionadas: salto con ↑, iniciar con ESPACIO.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            bird.jump();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = false;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    /**
     * Retorna si el juego está pausado.
     */
    public boolean paused() {
        return paused;
    }

    /**
     * Acceso a los power-ups actuales.
     */
    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Acceso a los defensores actuales.
     */
    public ArrayList<Defender> getDefenders() {
        return defenders;
    }

    /**
     * Reinicia el estado del juego a valores iniciales.
     */
    public void resetGame() {
        bird.reset();
        pipes.clear();
        powerUps.clear();
        defenders.clear();
        time = 0;
        scroll = 0;
        paused = true;
    }
}
