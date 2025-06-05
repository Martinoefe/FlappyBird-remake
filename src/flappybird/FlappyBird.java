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
 * Controla la lógica del juego, generación de obstáculos y power-ups,
 * físicas del pájaro, colisiones con objetos y gestión del estado "MiniMessi".
 */
public class FlappyBird implements ActionListener, KeyListener {

    // Constantes generales
    public static final int FPS = 80;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // Componentes y variables del juego
    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Pipe> pipes;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Defender> defenders;
    private ArrayList<MiniMessi> miniMessis;

    // Tiempos para controlar generación de defensores y bebidas
    private long lastDefenderSpawnTime = System.currentTimeMillis();
    private long lastMiniSpawnTime     = System.currentTimeMillis();

    private int time;    // Tiempo transcurrido (puntaje)
    private int scroll;  // Desplazamiento lateral

    private Timer t;
    private boolean paused;

    // Imágenes para las tuberías
    private Image pipeHead;
    private Image pipeLength;

    /**
     * Inicializa y lanza la ventana del juego.
     */
    public void go() {
        frame = new JFrame("Flappy Bird");
        bird  = new Bird();
        pipes       = new ArrayList<>();
        powerUps    = new ArrayList<>();
        defenders   = new ArrayList<>();
        miniMessis  = new ArrayList<>();

        // Cargar imágenes de tubería
        try {
            pipeHead   = ImageIO.read(new File("images/pipe.png"));
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
        frame.addKeyListener(this);

        paused = true; // El juego inicia pausado

        // Timer que actualiza el juego FPS veces por segundo
        t = new Timer(1000 / FPS, this);
        t.start();
    }

    /**
     * Método principal: lanza el juego.
     */
    public static void main(String[] args) {
        new FlappyBird().go();
    }

    /**
     * Lógica ejecutada en cada frame del juego (FPS veces por segundo).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();

        if (!paused) {
            bird.physics();
            generarTuberias();
            generarGoldenBall();
            generarDefensor();
            generarMiniMessi();
            actualizarTuberiasYDetectarColisiones();
            actualizarPowerUps();
            actualizarDefensores();
            actualizarMiniMessis();
            verificarCaidaDelPajaro();
            time++;
            scroll++;
        }
    }

    /**
     * Genera nuevas tuberías cada 90 frames.
     * Cada par de tuberías se crea con alturas aleatorias dentro de un rango.
     */
    private void generarTuberias() {
        if (scroll % 90 == 0) {
            int h1 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            int h2 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true,  pipeHead, pipeLength));
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
                yPowerUp = (int)(Math.random() * (HEIGHT - 100));
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
     * El defensor se ubica inicialmente fuera de pantalla a la derecha.
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
     * Genera una bebida MiniMessi cada 10000 ms (10 segundos).
     * El power-up se dibuja como bebida y, al colisionar, hará al pájaro mini sin mostrar diálogo.
     */
    private void generarMiniMessi() {
        if (System.currentTimeMillis() - lastMiniSpawnTime >= 10000) {
            int spawnX = WIDTH + 50;
            int spawnY = (int)(Math.random() * (HEIGHT - 50));
            miniMessis.add(new MiniMessi(spawnX, spawnY));
            lastMiniSpawnTime = System.currentTimeMillis();
        }
    }

    /**
     * Actualiza las tuberías: las mueve, verifica colisiones con el pájaro y elimina las que están fuera.
     * Si el pájaro choca (y no está invencible), se muestra mensaje y se reinicia el juego.
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
     * Actualiza los power-ups GoldenBall: los mueve, aplica efecto al pájaro y elimina si corresponde.
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
     * Actualiza los defensores: los mueve, verifica colisiones con el pájaro y elimina si sale de pantalla.
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
     * Actualiza cada MiniMessi: lo mueve, verifica colisión con el pájaro y lo elimina si corresponde.
     * Al colisionar, se activa el modo mini en Bird sin mostrar ningún cartel emergente.
     */
    private void actualizarMiniMessis() {
        ArrayList<MiniMessi> toRemove = new ArrayList<>();
        for (MiniMessi m : miniMessis) {
            m.update();
            if (m.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                m.applyEffect(bird);
                toRemove.add(m);
                // Aquí NO mostramos JOptionPane: al tomar bebida, simplemente se hace mini.
                continue;
            }
            if (m.isOffScreen()) {
                toRemove.add(m);
            }
        }
        miniMessis.removeAll(toRemove);
    }

    /**
     * Verifica si el pájaro ha caído fuera de los límites superior o inferior del juego.
     * Si ocurre, muestra puntaje y reinicia la partida.
     */
    private void verificarCaidaDelPajaro() {
        if (bird.y > HEIGHT || bird.y + Bird.HEIGHT / 2 < 0) {
            JOptionPane.showMessageDialog(frame, "Te caíste.\nTu puntaje fue: " + time);
            paused = true;
            resetGame();
        }
    }

    /**
     * @return el puntaje (número de frames transcurridos).
     */
    public int getScore() {
        return time;
    }

    /**
     * Manejador de tecla presionada: ↑ para saltar, SPACE para iniciar/reanudar.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            bird.jump();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    /**
     * @return true si el juego está en pausa.
     */
    public boolean paused() {
        return paused;
    }

    /**
     * @return la lista de power-ups GoldenBall actualmente activos.
     */
    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * @return la lista de defensores actualmente activos.
     */
    public ArrayList<Defender> getDefenders() {
        return defenders;
    }

    /**
     * @return la lista de bebidas MiniMessi actualmente activas.
     */
    public ArrayList<MiniMessi> getMiniMessis() {
        return miniMessis;
    }

    /**
     * Reinicia el estado del juego a los valores iniciales.
     */
    public void resetGame() {
        bird.reset();
        pipes.clear();
        powerUps.clear();
        defenders.clear();
        miniMessis.clear();
        time = 0;
        scroll = 0;
        paused = true;
    }
}
