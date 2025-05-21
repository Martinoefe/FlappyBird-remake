package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private FlappyBird fb;
    private Font scoreFont, pauseFont;

    public static final Color bg = new Color(0, 158, 158);
    public static final int PIPE_W = 50, PIPE_H = 30;

    public GamePanel(FlappyBird fb, Bird bird, ArrayList<Pipe> pipes) {
        this.fb = fb;
        this.bird = bird;
        this.pipes = pipes;

        scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);
        pauseFont = new Font("Arial", Font.BOLD, 48);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(bg);
        g.fillRect(0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);

        bird.update(g);

        for (PowerUp p : fb.getPowerUps()) {
            p.draw(g);
        }

        for (Pipe p : pipes) {
            p.draw(g);
        }

        g.setFont(scoreFont);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + fb.getScore(), 10, 20);

        if (fb.paused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(0, 0, 0, 170));
            g.drawString("PAUSED", FlappyBird.WIDTH / 2 - 100, FlappyBird.HEIGHT / 2 - 100);
            g.drawString("PRESS SPACE TO BEGIN", FlappyBird.WIDTH / 2 - 300, FlappyBird.HEIGHT / 2 + 50);
        }
    }
}
