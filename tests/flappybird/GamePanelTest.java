package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanelTest {

    private GamePanel panel;
    private GameModel model;

    @BeforeEach
    public void setUp() {
        model = new GameModel();
        panel = new GamePanel(model);
    }

    @Test
    public void testPanelConstruction() {
        assertNotNull(panel);
    }

    @Test
    public void testPaintComponentRunsWithoutError() {
        // Creamos un BufferedImage para simular el Graphics
        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        // Verificamos que no lance excepción al pintar
        assertDoesNotThrow(() -> panel.paintComponent(g));
    }

    @Test
    public void testPaintComponentWhilePaused() {
        model.setPaused(true);
        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        assertDoesNotThrow(() -> panel.paintComponent(g));
    }

    @Test
    public void testPaintComponentWhilePlaying() {
        model.setPaused(false);
        model.getBird().jump();
        model.updateGameFrame(); // actualiza estado
        BufferedImage fakeScreen = new BufferedImage(
                GameModel.WIDTH, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = fakeScreen.getGraphics();

        assertDoesNotThrow(() -> panel.paintComponent(g));
    }
}
