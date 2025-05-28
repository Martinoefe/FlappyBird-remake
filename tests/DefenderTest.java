import flappybird.Defender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

public class DefenderTest {

    private static final int TEST_X = 200;
    private Defender defender;

    @BeforeEach
    public void setUp() {
        defender = new Defender(TEST_X);
    }

    @Test
    public void testIsActiveAlwaysTrue() {
        assertTrue(defender.isActive(), "A defender should always be active while on screen");
    }

    @Test
    public void testUpdateMovesLeftByTwoPixels() {
        int initialX = TEST_X;
        defender.update();
        // each update() moves x left by 2
        // use reflection if needed, but we know getBounds uses x, so extract it:
        Rectangle bounds = defender.getBounds();
        // x in bounds is (x + (width - hitboxWidth)/2)
        int expectedXInBounds = (initialX - 2) + (50 - 40) / 2;
        assertEquals(expectedXInBounds, bounds.x,
                "After one update(), the defender's X should decrease by 2 pixels");
    }

    @Test
    public void testOffScreenDetection() {
        // if x + width < 0 => off screen
        Defender off = new Defender(-100);
        assertTrue(off.isOffScreen(), "Defender starting off-screen (x=-100) should be considered off-screen");
    }

    @Test
    public void testGetBoundsDimensions() {
        Rectangle bounds = defender.getBounds();
        assertEquals(40, bounds.width, "Hitbox width should be 40");
        assertEquals(50, bounds.height, "Hitbox height should be 50");
    }
}
