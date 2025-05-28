import flappybird.Pipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    private Pipe topPipe;
    private Pipe bottomPipe;
    private final int screenHeight = 800;

    @BeforeEach
    void setUp() {
        // Simulación de dimensiones
        topPipe = new Pipe(300, 50, 200, true, null, null);
        bottomPipe = new Pipe(300, 50, 200, false, null, null);
    }

    @Test
    void testInitialPositionAndSize() {
        assertEquals(300, topPipe.getX());
        assertEquals(50, topPipe.getWidth());
        assertEquals(200, topPipe.getHeight());

        assertEquals(300, bottomPipe.getX());
        assertEquals(50, bottomPipe.getWidth());
        assertEquals(200, bottomPipe.getHeight());
    }

    @Test
    void testUpdateMovesLeft() {
        topPipe.update();
        bottomPipe.update();

        assertEquals(297, topPipe.getX());
        assertEquals(297, bottomPipe.getX());
    }

    @Test
    void testIsOffScreenFalseWhenVisible() {
        assertFalse(topPipe.isOffScreen());
        assertFalse(bottomPipe.isOffScreen());
    }

    @Test
    void testIsOffScreenTrueWhenOutside() {
        Pipe p = new Pipe(-60, 50, 100, true, null, null); // x + width = -10
        assertTrue(p.isOffScreen());
    }

    @Test
    void testGetBoundsTopPipe() {
        Rectangle expected = new Rectangle(300, 0, 50, 200);
        assertEquals(expected, topPipe.getBounds());
    }
/*
    @Test
    void testGetBoundsBottomPipe() {
        // FlappyBird.HEIGHT no es accesible, simulamos con una constante
        int expectedY = screenHeight - 200;
        Rectangle expected = new Rectangle(300, expectedY, 50, 200);
        Rectangle actual = bottomPipe.getBounds();

        assertEquals(expected.x, actual.x);
        assertEquals(expected.y, actual.y);
        assertEquals(expected.width, actual.width);
        assertEquals(expected.height, actual.height);
    }
    
 */
}