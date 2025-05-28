import flappybird.Bird;
import flappybird.Defender;
import flappybird.FlappyBird;
import flappybird.GamePanel;
import flappybird.Pipe;
import flappybird.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GamePanelTest {

    private static final int WIDTH = FlappyBird.WIDTH;
    private static final int HEIGHT = FlappyBird.HEIGHT;

    private GamePanel panel;
    private StubGame game;
    private Bird bird;
    private ArrayList<Pipe> pipes;

    /* Stub de FlappyBird para tests */
    private static class StubGame extends FlappyBird {
        private ArrayList<PowerUp> powerUps = new ArrayList<>();
        private ArrayList<Defender> defenders = new ArrayList<>();
        private int score = 0;
        private boolean paused = false;

        public StubGame() {
            super();  // Llamada al constructor sin parámetros
        }

        @Override
        public ArrayList<PowerUp> getPowerUps() {
            return powerUps;
        }

        @Override
        public ArrayList<Defender> getDefenders() {
            return defenders;
        }

        @Override
        public int getScore() {
            return score;
        }

        @Override
        public boolean paused() {
            return paused;
        }
    }

    @BeforeEach
    public void setUp() {
        game = new StubGame();
        bird = new Bird();
        pipes = new ArrayList<>();
        panel = new GamePanel(game, bird, pipes);
        panel.setSize(WIDTH, HEIGHT);
    }

    @Test
    public void testPaintComponentDoesNotThrow() {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        assertDoesNotThrow(() -> panel.paintComponent(g2d),
                "paintComponent should not throw any exception");
    }

    @Test
    public void testScrollXAdvancesBySpeed() throws Exception {
        Field scrollField = GamePanel.class.getDeclaredField("scrollX");
        scrollField.setAccessible(true);

        int initial = scrollField.getInt(panel);
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        panel.paintComponent(img.getGraphics());

        int after = scrollField.getInt(panel);
        int speed = getStaticField("SCROLL_SPEED");
        assertEquals(initial - speed, after,
                "scrollX should decrease by SCROLL_SPEED after one paintComponent call");
    }

    @Test
    public void testScrollXResetsWhenBeyondImageWidth() throws Exception {
        Field scrollField = GamePanel.class.getDeclaredField("scrollX");
        Field bgField     = GamePanel.class.getDeclaredField("backgroundImg");
        scrollField.setAccessible(true);
        bgField.setAccessible(true);

        BufferedImage background = (BufferedImage) bgField.get(panel);
        int imgWidth = background.getWidth();

        scrollField.setInt(panel, -imgWidth);
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        panel.paintComponent(img.getGraphics());

        int after = scrollField.getInt(panel);
        assertEquals(0, after, "scrollX should reset to 0 when <= -image width");
    }

    /* Refleja un campo estático en GamePanel */
    private int getStaticField(String name) throws Exception {
        Field f = GamePanel.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(null);
    }
}