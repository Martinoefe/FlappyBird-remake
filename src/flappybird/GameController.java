package flappybird;

import java.awt.event.*;
import javax.swing.*;

public class GameController implements ActionListener, KeyListener {
    private GameModel model;
    private GamePanel view;
    private JFrame frame;
    private Timer timer;

    public GameController() {
        model = new GameModel();
        view  = new GamePanel(model);

        frame = new JFrame("Flappy Bird (MVC + Singleton)");
        frame.add(view);
        frame.setSize(GameModel.WIDTH, GameModel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);

        timer = new Timer(1000 / GameModel.FPS, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!model.isPaused()) {
            model.updateGameFrame();
            if (model.isPaused()) {
                // Si el modelo quedó en paused==true debido a “game over”
                JOptionPane.showMessageDialog(frame,
                    "Game Over\nYour Score: " + model.getScore());
                model.resetGame();
            }
        }
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            model.birdJump();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            model.setPaused(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { /* No usado */ }

    @Override
    public void keyTyped(KeyEvent e) { /* No usado */ }

    public static void main(String[] args) {
        new GameController();
    }
}
