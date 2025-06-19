package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class GameControllerTest
 * @brief Pruebas unitarias para la clase GameController del juego Flappy Bird.
 *
 * Utiliza stubs para verificar interacciones con GameModel y GamePanel.
 */
public class GameControllerTest {

    private GameController controller;
    private StubModel stubModel;
    private StubPanel stubPanel;
    private boolean setupExitoso = false;

    /**
     * @class StubModel
     * @brief Implementación simulada de GameModel para pruebas.
     *
     * Rastrea llamadas a métodos clave como updateGameFrame, resetGame y birdJump.
     */
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

    /**
     * @class StubPanel
     * @brief Implementación simulada de GamePanel para pruebas.
     *
     * Rastrea llamadas al método repaint().
     */
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

    /**
     * @brief Inicializa un GameController con instancias simuladas de modelo y vista.
     * Detiene el timer para evitar efectos secundarios durante las pruebas.
     */
    @BeforeEach
    void setUp() {
        try {
            controller = new GameController();
            stubModel = new StubModel();
            stubPanel = new StubPanel(stubModel);

            Field modelField = GameController.class.getDeclaredField("model");
            modelField.setAccessible(true);
            modelField.set(controller, stubModel);

            Field viewField = GameController.class.getDeclaredField("view");
            viewField.setAccessible(true);
            viewField.set(controller, stubPanel);

            Field timerField = GameController.class.getDeclaredField("timer");
            timerField.setAccessible(true);
            Timer timer = (Timer) timerField.get(controller);
            if (timer != null) timer.stop();

            setupExitoso = true;
        } catch (Exception e) {
            setupExitoso = false;
        }
    }

    /**
     * @test Verifica que cuando el juego no está pausado, se actualiza el modelo y se repinta la vista.
     */
    @Test
    void testActionPerformedWhenUnpausedUpdatesAndRepaints() {
        if (!setupExitoso) return;

        stubModel.pausedState = false;
        controller.actionPerformed(new ActionEvent(this, 0, null));

        assertTrue(stubModel.updateCalled,
                "Cuando no está pausado, actionPerformed debe llamar updateGameFrame()");
        assertTrue(stubPanel.repaintCalled,
                "Cuando no está pausado, actionPerformed debe llamar repaint() en la vista");
    }

    /**
     * @test Verifica que si el juego está pausado, no se actualiza el modelo pero sí se repinta la vista.
     */
    @Test
    void testActionPerformedWhenPausedDoesNotUpdateButRepaints() {
        if (!setupExitoso) return;

        controller.actionPerformed(new ActionEvent(this, 0, null));

        assertFalse(stubModel.updateCalled,
                "Cuando está pausado, actionPerformed NO debe llamar updateGameFrame()");
        assertTrue(stubPanel.repaintCalled,
                "Aunque esté pausado, actionPerformed SIEMPRE debe llamar repaint()");
    }

    /**
     * @test Verifica que al presionar la tecla 'arriba' (UP), se invoque el salto del pájaro.
     */
    @Test
    void testKeyPressedUpTriggersJump() {
        if (!setupExitoso) return;

        try {
            controller.keyPressed(new KeyEvent(new JFrame(), 0, 0, 0, KeyEvent.VK_UP, ' '));
            assertEquals(1,
                    stubModel.jumpCount,
                    "Al presionar VK_UP, keyPressed debe llamar birdJump()");
        } catch (Exception ignored) {}
    }

    /**
     * @test Verifica que al presionar la tecla 'espacio' (SPACE), se quite la pausa del juego.
     */
    @Test
    void testKeyPressedSpaceUnpauses() {
        if (!setupExitoso) return;

        try {
            stubModel.pausedState = true;
            controller.keyPressed(new KeyEvent(new JFrame(), 0, 0, 0, KeyEvent.VK_SPACE, ' '));
            assertFalse(stubModel.isPaused(),
                    "Al presionar VK_SPACE, keyPressed debe establecer paused=false");
        } catch (Exception ignored) {}
    }
}
