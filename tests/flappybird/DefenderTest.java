package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

public class DefenderTest {
    private Defender defender;

    @BeforeEach
    public void setUp() {
        defender = new Defender(800); // Lo colocamos en el borde derecho de la pantalla
    }

    @Test
    public void testInitialPositionWithinBounds() {
        Rectangle bounds = defender.getBounds();
        assertTrue(bounds.getY() >= DefenderTestConstants.LIMIT_TOP);
        assertTrue(bounds.getY() <= DefenderTestConstants.LIMIT_BOTTOM);
    }

    @Test
    public void testUpdateStateMovesLeft() {
        float initialX = defender.getBounds().x;
        defender.updateState();
        float updatedX = defender.getBounds().x;
        assertTrue(updatedX < initialX);
    }

    @Test
    public void testVerticalReboundAtLimits() {
        // Forzamos múltiples actualizaciones para alcanzar límites y rebotar
        for (int i = 0; i < 300; i++) {
            defender.updateState();
        }
        Rectangle bounds = defender.getBounds();
        assertTrue(bounds.getY() >= DefenderTestConstants.LIMIT_TOP);
        assertTrue(bounds.getY() <= DefenderTestConstants.LIMIT_BOTTOM);
    }

    @Test
    public void testIsOffScreenReturnsTrueWhenOutOfBounds() {
        // Movemos el defensor fuera de pantalla a la izquierda
        for (int i = 0; i < 500; i++) {
            defender.updateState();
        }
        assertTrue(defender.isOffScreen());
    }

    @Test
    public void testGetBoundsHasCorrectSize() {
        Rectangle bounds = defender.getBounds();
        assertEquals(40, bounds.getWidth());
        assertEquals(50, bounds.getHeight());
    }
}
