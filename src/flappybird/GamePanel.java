package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * @class GamePanel
 * @brief Panel Swing principal donde se dibuja todo el juego.
 *
 * - Dibuja el fondo con scroll infinito.
 * - Pinta Bird, PowerUps, Pipes y Defenders.
 * - Muestra el puntaje y mensajes de pausa.
 */
public class GamePanel extends JPanel {
    private GameModel        model;            ///< referencia al modelo de juego
    private BufferedImage    backgroundImg;    ///< imagen de fondo
    private int              scrollX      = 0; ///< desplazamiento horizontal acumulado
    private static final int SCROLL_SPEED = 2; ///< velocidad de scroll del fondo
    private Font             scoreFont    =    ///< fuente para puntaje
        new Font("Comic Sans MS", Font.BOLD, 18);
    private Font pauseFont = ///< fuente para mensaje de pausa
        new Font("Arial", Font.BOLD, 48);

    public static final int PIPE_W = 50; ///< ancho estándar de tubería

    /**
     * @brief Constructor.
     * Carga la imagen de fondo y guarda la referencia al modelo.
     * @param model instancia de GameModel
     */
    public GamePanel(GameModel model) {
        this.model = model;
        try {
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Dibuja todos los elementos del juego.
     * - Fondo (con scroll), Bird, PowerUps, Pipes, Defenders.
     * - Puntaje en esquina y texto de pausa si aplica.
     * @param g contexto gráfico
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // scroll y dibujo de fondo
        if ( backgroundImg != null ) {
            int imgW = backgroundImg.getWidth();
            scrollX -= SCROLL_SPEED;
            if ( scrollX <= -imgW )
                scrollX = 0;
            for ( int x = scrollX; x < GameModel.WIDTH; x += imgW ) {
                g.drawImage(backgroundImg, x, 0, GameModel.WIDTH, GameModel.HEIGHT, null);
            }
        } else {
            g.setColor(new Color(0, 158, 158));
            g.fillRect(0, 0, GameModel.WIDTH, GameModel.HEIGHT);
        }

        // dibuja entidades
        model.getBird().draw(g);
        for ( PowerUp pu : model.getPowerUps() )
            pu.draw(g);
        for ( Pipe p : model.getPipes() )
            p.draw(g);
        for ( Defender d : model.getDefenders() )
            d.draw(g);

        // dibuja puntaje
        g.setFont(scoreFont);
        g.setColor(Color.white);
        g.drawString("Score: " + model.getScore(), 10, 20);

        // dibuja mensaje de pausa si corresponde
        if ( model.isPaused() ) {
            g.setFont(pauseFont);
            g.setColor(new Color(255, 255, 255, 200));
            g.drawString("PAUSED", GameModel.WIDTH / 2 - 100, GameModel.HEIGHT / 2 - 100);
            g.drawString(
                "PRESS SPACE TO BEGIN", GameModel.WIDTH / 2 - 300, GameModel.HEIGHT / 2 + 50);
        }
    }
}
