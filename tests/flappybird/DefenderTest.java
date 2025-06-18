package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class DefenderTest {

    private static final int START_X = 100;
    private Defender defender;

    @BeforeEach
    void setUp() {
        defender = new Defender(START_X);
    }

    @Test
    void testUpdateStateMovesLeftByTwo() throws Exception {
        float initialX = getFieldFloat(defender, "x");
        defender.updateState();
        float afterX = getFieldFloat(defender, "x");
        assertEquals(initialX - 2, afterX, 0.001, "updateState() debe mover x en -2");
    }

    @Test
    void testBounceAtTopResetsYAndDirection() throws Exception {
        // Forzar y por encima del límite superior
        Field yField = Defender.class.getDeclaredField("y");
        yField.setAccessible(true);
        yField.setFloat(defender, -5f);

        defender.updateState();

        float yAfter = getFieldFloat(defender, "y");
        int direction = getFieldInt(defender, "direction");
        int limitTop = getStaticField("LIMIT_TOP");
        assertEquals(limitTop, yAfter, 0.001, "Al rebotar en top, y debe ajustarse a LIMIT_TOP");
        assertEquals(1, direction, "La dirección debe cambiar a 1 tras rebotar en el top");
    }

    @Test
    void testBounceAtBottomResetsYAndDirection() throws Exception {
        // Forzar y por debajo del límite inferior
        int limitBottom = getStaticField("LIMIT_BOTTOM");
        Field yField = Defender.class.getDeclaredField("y");
        yField.setAccessible(true);
        yField.setFloat(defender, limitBottom + 5f);

        defender.updateState();

        float yAfter = getFieldFloat(defender, "y");
        int direction = getFieldInt(defender, "direction");
        assertEquals(limitBottom, yAfter, 0.001, "Al rebotar en bottom, y debe ajustarse a LIMIT_BOTTOM");
        assertEquals(-1, direction, "La dirección debe cambiar a -1 tras rebotar en el bottom");
    }

    @Test
    void testGetBounds() throws Exception {
        // Forzar posición conocida
        setFieldFloat(defender, "x", 10f);
        setFieldFloat(defender, "y", 20f);

        Rectangle bounds = defender.getBounds();
        assertEquals(10 + 5, bounds.x, "getBounds.x debe ser x+5");
        assertEquals(20, bounds.y, "getBounds.y debe ser y");
        assertEquals(40, bounds.width, "getBounds.width debe ser 40");
        assertEquals(50, bounds.height, "getBounds.height debe ser 50");
    }

    @Test
    void testIsOffScreenTrue() {
        Defender off = new Defender(-100);
        assertTrue(off.isOffScreen(), "isOffScreen() debe ser true si x+width<0");
    }

    @Test
    void testIsOffScreenFalse() {
        assertFalse(defender.isOffScreen(), "isOffScreen() debe ser false si aún es visible");
    }

    @Test
    void testDrawDoesNotThrow() {
        BufferedImage img = new BufferedImage(200, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        assertDoesNotThrow(() -> defender.draw(g2d), "draw() no debe lanzar excepción");
    }

    // Helpers para reflección
    private float getFieldFloat(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.getFloat(obj);
    }

    private int getFieldInt(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(obj);
    }

    private void setFieldFloat(Object obj, String name, float value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.setFloat(obj, value);
    }

    private int getStaticField(String name) throws Exception {
        Field f = Defender.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(null);
    }
}
