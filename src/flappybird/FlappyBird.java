package flappybird;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class FlappyBird implements ActionListener, KeyListener {

    public static final int FPS = 60, WIDTH = 640, HEIGHT = 480;

    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Pipe> pipes;
    private int time, scroll;
    private Timer t;
    private ArrayList<PowerUp> powerUps;

    private boolean paused;

    // Imágenes de los tubos
    private Image pipeHead, pipeLength;

    public void go() {
        frame = new JFrame("Flappy Bird");
        bird = new Bird();
        pipes = new ArrayList<>();
        powerUps = new ArrayList<>();

        // Cargar imágenes una vez
        try {
            pipeHead = ImageIO.read(new File("images/pipe.png"));
            pipeLength = ImageIO.read(new File("images/pipe_part.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel = new GamePanel(this, bird, pipes);
        frame.add(panel);

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);

        paused = true;

        t = new Timer(1000 / FPS, this);
        t.start();
    }

    public static void main(String[] args) {
        new FlappyBird().go();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();
        if (!paused) {
            bird.physics();

            // Generación de tuberías cada cierto tiempo
            if (scroll % 90 == 0) {
                int h1 = (int) ((Math.random() * HEIGHT) / 5f + (0.2f) * HEIGHT);
                int h2 = (int) ((Math.random() * HEIGHT) / 5f + (0.2f) * HEIGHT);
                pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true, pipeHead, pipeLength));
                pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h2, false, pipeHead, pipeLength));
            }

            // Generación de GoldenBall cada cierto tiempo
            if (scroll % 900 == 0) {
                int yPowerUp = (int) (Math.random() * (HEIGHT - 100));
                powerUps.add(new GoldenBall(WIDTH, yPowerUp));
            }

            ArrayList<Pipe> toRemove = new ArrayList<>();
            ArrayList<PowerUp> toRemovePowerUps = new ArrayList<>();
            boolean game = true;

            // Actualizar y verificar colisiones con tuberías
            for (Pipe p : pipes) {
                p.update();
                if (p.isOffScreen()) {
                    toRemove.add(p);
                }
                if (p.getBounds().contains(bird.x, bird.y)) {
                    if (!bird.isInvincible()) {
                        JOptionPane.showMessageDialog(frame, "You lose!\nYour score was: " + time + ".");
                        game = false;
                    }
                }
            }

            // Actualizar y verificar colisiones con power-ups
            for (PowerUp p : powerUps) {
                p.update();
                if (p.getBounds().intersects(new Rectangle((int) bird.x - Bird.RAD, (int) bird.y - Bird.RAD, 2 * Bird.RAD, 2 * Bird.RAD))) {
                    p.applyEffect(bird);
                    toRemovePowerUps.add(p);
                }
                if (p instanceof GoldenBall && ((GoldenBall) p).isOffScreen()) {
                    toRemovePowerUps.add(p);
                }
            }

            pipes.removeAll(toRemove);
            powerUps.removeAll(toRemovePowerUps);

            time++;
            scroll++;

            if (bird.y > HEIGHT || bird.y + bird.RAD < 0) {
                game = false;
            }

            if (!game) {
                pipes.clear();
                powerUps.clear();
                bird.reset();
                time = 0;
                scroll = 0;
                paused = true;
            }
        }
    }

    public int getScore() {
        return time;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            bird.jump();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean paused() {
        return paused;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
