package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private GameModel model;
    private BufferedImage backgroundImg;
    private int scrollX = 0;
    private static final int SCROLL_SPEED = 2;
    private Font scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);
    private Font pauseFont = new Font("Arial", Font.BOLD, 48);

    public static final int PIPE_W = 50;

    public GamePanel(GameModel model) {
        this.model = model;
        try {
            backgroundImg = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) Dibujo del fondo con scroll
        if (backgroundImg != null) {
            int imgW = backgroundImg.getWidth();
            scrollX -= SCROLL_SPEED;
            if (scrollX <= -imgW) {
                scrollX = 0;
            }
            for (int x = scrollX; x < GameModel.WIDTH; x += imgW) {
                g.drawImage(backgroundImg, x, 0, GameModel.WIDTH, GameModel.HEIGHT, null);
            }
        } else {
            g.setColor(new Color(0, 158, 158));
            g.fillRect(0, 0, GameModel.WIDTH, GameModel.HEIGHT);
        }

        // 2) Dibujar Bird
        model.getBird().draw(g);

        // 3) Dibujar power-ups (GoldenBall y MiniMessi)
        for (PowerUp pu : model.getPowerUps()) {
            pu.draw(g);
        }

        // 4) Dibujar Pipes
        for (Pipe p : model.getPipes()) {
            p.draw(g);
        }

        // 5) Dibujar Defenders
        for (Defender d : model.getDefenders()) {
            d.draw(g);
        }

        // 6) Dibujar puntaje en esquina superior izquierda
        g.setFont(scoreFont);
        g.setColor(Color.white);
        g.drawString("Score: " + model.getScore(), 10, 20);

        // 7) Si está en pausa, mostrar texto semi-transparente
        if (model.isPaused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(255, 255, 255, 200));
            g.drawString("PAUSED", GameModel.WIDTH/2 - 100, GameModel.HEIGHT/2 - 100);
            g.drawString("PRESS SPACE TO BEGIN", GameModel.WIDTH/2 - 300, GameModel.HEIGHT/2 + 50);
        }
    }
}
