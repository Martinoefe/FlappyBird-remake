package flappybird;

import java.awt.event.*;
import javax.swing.*;

/**
 * @class GameController
 * @brief Controla la interacción entre GUI y modelo, y gestiona el bucle principal.
 *
 * - Captura eventos de teclado para saltos y para reanudar desde pausa.
 * - Recibe ticks del Timer para avanzar el modelo y repintar la vista.
 * - Muestra un diálogo de “Game Over” al perder, y resetea el juego.
 */
public class GameController implements ActionListener, KeyListener {
    private GameModel model; ///< Lógica de negocio del juego
    private GamePanel view;  ///< Panel Swing donde se dibuja
    private JFrame    frame; ///< Ventana principal
    private Timer     timer; ///< Generador de ticks a 1000/FPS ms

    /**
     * @brief Constructor.
     * Inicializa modelo, vista, ventana y arranca el Timer.
     */
    public GameController() {
        model = new GameModel();
        view  = new GamePanel(model);

        frame = new JFrame("Flappy Messi");
        frame.add(view);
        frame.setSize(GameModel.WIDTH, GameModel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);

        timer = new Timer(1000 / GameModel.FPS, this);
        timer.start();
    }

    /**
     * @brief Se llama en cada tick del Timer.
     * - Si no está en pausa, actualiza el modelo.
     * - Si el modelo entra en pausa por “game over”, muestra diálogo y resetea.
     * - Finalmente repinta la vista.
     * @param e evento de acción generado por el Timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( !model.isPaused() ) {
            model.updateGameFrame();
            if ( model.isPaused() ) {
                JOptionPane.showMessageDialog(frame, "Game Over\nYour Score: " + model.getScore());
                model.resetGame();
            }
        }
        view.repaint();
    }

    /**
     * @brief Captura teclas.
     * - VK_UP → hace saltar al pájaro.
     * - VK_SPACE → quita la pausa.
     * @param e evento de teclado
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            model.birdJump();
        } else if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            model.setPaused(false);
        }
    }

    /** @brief No usado. */
    @Override public void keyReleased(KeyEvent e) { /* No usado */ }

    /** @brief No usado. */
    @Override public void keyTyped(KeyEvent e) { /* No usado */ }

    /**
     * @brief Punto de entrada.
     * Inicia el controlador, que a su vez arranca todo el juego.
     * @param args argumentos ignorados
     */
    public static void main(String[] args) {
        new GameController();
    }
}
