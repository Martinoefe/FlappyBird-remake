package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameController controller;
    private StubModel stubModel;
    private StubPanel stubPanel;

    /** Un GameModel de prueba que cuenta llamadas */
    private static class StubModel extends GameModel {
        boolean updateCalled = false;
        boolean resetCalled = false;
        boolean pausedState = true;
        int jumpCount = 0;

        @Override
        public boolean isPaused() {
            return pausedState;
        }

        @Override
        public void updateGameFrame() {
            updateCalled = true;
            // Simular continuación sin game over
            pausedState = false;
        }

        @Override
        public void resetGame() {
            resetCalled = true;
        }

        @Override
        public void birdJump() {
            jumpCount++;
        }

        @Override
        public void setPaused(boolean p) {
            pausedState = p;
        }
    }

    /** Un GamePanel de prueba que rastrea repaint() */
    private static class StubPanel extends GamePanel {
        boolean repaintCalled = false;

        StubPanel(GameModel model) {
            super(model);
        }

        @Override
        public void repaint() {
            repaintCalled = true;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new GameController();
        stubModel = new StubModel();
        stubPanel = new StubPanel(stubModel);

        // Inyectar stubModel
        Field modelField = GameController.class.getDeclaredField("model");
        modelField.setAccessible(true);
        modelField.set(controller, stubModel);

        // Inyectar stubPanel como view
        Field viewField = GameController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(controller, stubPanel);

        // Detener el timer para que no dispare durante los tests
        Field timerField = GameController.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        ((Timer) timerField.get(controller)).stop();
    }

    @Test
    void testActionPerformedWhenUnpausedUpdatesAndRepaints() {
        // Simular que no está pausado
        stubModel.pausedState = false;

        controller.actionPerformed(new ActionEvent(this, 0, null));

        assertTrue(stubModel.updateCalled,
                "Cuando no está pausado, actionPerformed debe llamar updateGameFrame()");
        assertTrue(stubPanel.repaintCalled,
                "Cuando no está pausado, actionPerformed debe llamar repaint() en la vista");
    }

    @Test
    void testActionPerformedWhenPausedDoesNotUpdateButRepaints() {
        // Dejar pausedState = true (estado inicial)
        controller.actionPerformed(new ActionEvent(this, 0, null));

        assertFalse(stubModel.updateCalled,
                "Cuando está pausado, actionPerformed NO debe llamar updateGameFrame()");
        assertTrue(stubPanel.repaintCalled,
                "Aunque esté pausado, actionPerformed SIEMPRE debe llamar repaint()");
    }

    @Test
    void testKeyPressedUpTriggersJump() {
        controller.keyPressed(new KeyEvent(new JFrame(), 0, 0, 0, KeyEvent.VK_UP, ' '));
        assertEquals(1,
                stubModel.jumpCount,
                "Al presionar VK_UP, keyPressed debe llamar birdJump()");
    }

    @Test
    void testKeyPressedSpaceUnpauses() {
        stubModel.pausedState = true;
        controller.keyPressed(new KeyEvent(new JFrame(), 0, 0, 0, KeyEvent.VK_SPACE, ' '));
        assertFalse(stubModel.isPaused(),
                "Al presionar VK_SPACE, keyPressed debe establecer paused=false");
    }
}
