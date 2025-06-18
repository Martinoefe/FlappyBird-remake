package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class PipeTest {

    private static final int START_X = 100;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 200;

    private BufferedImage dummyHead;
    private BufferedImage dummyBody;
    private Pipe topPipe;
    private Pipe bottomPipe;

    @BeforeEach
    void setUp() {
        // Creamos imágenes dummy para el test
        dummyHead = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        dummyBody = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        // Usamos el constructor que recibe imágenes
        topPipe = new Pipe(START_X, WIDTH, HEIGHT, true, dummyHead, dummyBody);
        bottomPipe = new Pipe(START_X, WIDTH, HEIGHT, false, dummyHead, dummyBody);
    }

    @Test
    void testUpdateStateMovesLeftByThree() {
        float initialX = topPipe.getX();
        topPipe.updateState();
        assertEquals(initialX - 3, topPipe.getX(), 0.001,
                "updateState() debe reducir x en 3");
    }

    @Test
    void testGetXReflectsPosition() {
        assertEquals(START_X, topPipe.getX(), 0.001);
    }

    @Test
    void testGetBoundsTop() {
        Rectangle bounds = topPipe.getBounds();
        // Para tubería superior, Y = 0
        assertEquals(START_X, bounds.x);
        assertEquals(0, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);
    }

    @Test
    void testGetBoundsBottom() {
        Rectangle bounds = bottomPipe.getBounds();
        // Para tubería inferior, Y = GameModel.HEIGHT - height
        int expectedY = GameModel.HEIGHT - HEIGHT;
        assertEquals(START_X, bounds.x);
        assertEquals(expectedY, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);
    }

    @Test
    void testIsOffScreenTrueWhenLeft() {
        Pipe off = new Pipe(-WIDTH - 1, WIDTH, HEIGHT, true, dummyHead, dummyBody);
        assertTrue(off.isOffScreen(), "isOffScreen() debe ser true cuando x + width < 0");
    }

    @Test
    void testIsOffScreenFalseWhenVisible() {
        assertFalse(topPipe.isOffScreen(),
                "isOffScreen() debe ser false cuando la tubería aún es visible");
    }

    @Test
    void testDrawDoesNotThrow() {
        BufferedImage canvas = new BufferedImage(800, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();
        assertDoesNotThrow(() -> {
            topPipe.draw(g2d);
            bottomPipe.draw(g2d);
        }, "draw() no debe lanzar ninguna excepción");
    }
}
