package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @class GamePanelTest
 * @brief Pruebas unitarias para la clase GamePanel del juego Flappy Bird.
 *
 * Se verifica que el panel gráfico pueda construirse y renderizarse correctamente,
 * tanto en estado pausado como en ejecución.
 */
public class GamePanelTest {

    private GamePanel panel;
    private GameModel model;

    /**
     * @brief Inicializa el modelo y el panel antes de cada prueba.
     */
    @BeforeEach
    public void setUp() {
        model = new GameModel();
        panel = new GamePanel(model);
    }

    /**
     * @test Verifica que el panel se construya correctamente sin ser nulo.
     */
    @Test
    public void testPanelConstruction() {
        assertNotNull(panel);
    }

    /**
     * @test Verifica que el método paintComponent() no arroje errores durante la ejecución.
     */
    @Test
    public void testPaintComponentRunsWithoutError() {
        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        assertDoesNotThrow(() -> panel.paintComponent(g));
    }

    /**
     * @test Verifica que el panel se renderice correctamente mientras el juego está pausado.
     */
    @Test
    public void testPaintComponentWhilePaused() {
        model.setPaused(true);
        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        assertDoesNotThrow(() -> panel.paintComponent(g));
    }

    /**
     * @test Verifica que el panel se renderice correctamente mientras el juego está en ejecución.
     */
    @Test
    public void testPaintComponentWhilePlaying() {
        model.setPaused(false);
        model.getBird().jump();
        model.updateGameFrame();

        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        assertDoesNotThrow(() -> panel.paintComponent(g));
    }
}
