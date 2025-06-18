package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.*;

public class GameControllerTest {

    private GameController controller;
    private GameModel model;

    @BeforeEach
    public void setUp() {
        // Creamos una instancia "manual" sin mostrar la ventana
        controller = new GameController() {
            {
                model = new GameModel();
                view = new GamePanel(model);
                frame = new JFrame(); // sin mostrar
                timer = null; // evitamos usar el Timer real
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.isPaused()) {
                    model.updateGameFrame();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e); // usa la lógica real
            }

            // Permitir acceso desde test
            public GameModel getModel() {
                return model;
            }
        };

        model = ((GameController) controller).getModel();
    }

    @Test
    public void testSpaceKeyUnpausesGame() {
        assertTrue(model.isPaused());
        KeyEvent spaceKey = new KeyEvent(new DummyComponent(), 0, 0, 0, KeyEvent.VK_SPACE, ' ');
        controller.keyPressed(spaceKey);
        assertFalse(model.isPaused());
    }

    @Test
    public void testUpKeyCausesBirdJump() {
        float yBefore = model.getBird().getY();
        KeyEvent upKey = new KeyEvent(new DummyComponent(), 0, 0, 0, KeyEvent.VK_UP, ' ');
        controller.keyPressed(upKey);
        model.getBird().updateState();
        assertTrue(model.getBird().getY() < yBefore);
    }

    @Test
    public void testActionPerformedUpdatesGame() {
        model.setPaused(false);
        int scoreBefore = model.getScore();
        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        assertTrue(model.getScore() > scoreBefore);
    }

    // Dummy componente para simular eventos sin GUI
    private static class DummyComponent extends java.awt.Component {
        private static final long serialVersionUID = 1L;
    }
}
