package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private FlappyBird fb;

    private Font scoreFont, pauseFont;
    private BufferedImage backgroundImg;
    private int scrollX = 0;
    private static final int SCROLL_SPEED = 2;

    public static final int PIPE_W = 50, PIPE_H = 30;

    public GamePanel(FlappyBird fb, Bird bird, ArrayList<Pipe> pipes) {
        this.fb = fb;
        this.bird = bird;
        this.pipes = pipes;

        scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);
        pauseFont = new Font("Arial", Font.BOLD, 48);

        try {
            backgroundImg = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujo del fondo (scroll)
        if (backgroundImg != null) {
            int imgWidth = backgroundImg.getWidth();
            scrollX -= SCROLL_SPEED;
            if (scrollX <= -imgWidth) {
                scrollX = 0;
            }
            for (int x = scrollX; x < FlappyBird.WIDTH; x += imgWidth) {
                g.drawImage(backgroundImg, x, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT, null);
            }
        } else {
            g.setColor(new Color(0, 158, 158));
            g.fillRect(0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
        }

        // Dibujar al pájaro (Bird)
        bird.update(g);

        // Dibujar power-ups GoldenBall (si los hubiera)
        for (PowerUp p : fb.getPowerUps()) {
            p.draw(g);
        }

        // ─── Dibujar bebidas MiniMessi ───
        for (MiniMessi m : fb.getMiniMessis()) {
            m.draw(g);
        }

        // Dibujar tuberías
        for (Pipe p : pipes) {
            p.draw(g);
        }

        // Dibujar defensores
        for (Defender d : fb.getDefenders()) {
            d.draw(g);
        }

        // Dibujar puntaje
        g.setFont(scoreFont);
        g.setColor(Color.white);
        g.drawString("Score: " + fb.getScore(), 10, 20);

        // Si está en pausa, mostrar texto
        if (fb.paused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(255, 255, 255, 200));
            g.drawString("PAUSED", FlappyBird.WIDTH / 2 - 100, FlappyBird.HEIGHT / 2 - 100);
            g.drawString("PRESS SPACE TO BEGIN", FlappyBird.WIDTH / 2 - 300, FlappyBird.HEIGHT / 2 + 50);
        }
    }
}
