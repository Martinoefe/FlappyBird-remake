package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class MiniMessiTest {

    private static final int INITIAL_X = 300;
    private static final int INITIAL_Y = 150;
    private MiniMessi mini;

    @BeforeEach
    void setUp() {
        // Cada llamada devuelve la misma instancia, reposicionada
        mini = MiniMessi.getInstancia(INITIAL_X, INITIAL_Y);
    }

    @Test
    void testSingletonIdentity() {
        MiniMessi other = MiniMessi.getInstancia(10, 20);
        assertSame(mini, other, "MiniMessi debe comportarse como singleton");
    }

    @Test
    void testPositionAfterGetInstancia() {
        Rectangle bounds = mini.getBounds();
        assertEquals(INITIAL_X, bounds.x, "La coordenada X debe coincidir con la pasada");
        assertEquals(INITIAL_Y, bounds.y, "La coordenada Y debe coincidir con la pasada");
    }

    @Test
    void testUpdateMovesLeftByThree() {
        Rectangle before = mini.getBounds();
        mini.update();
        Rectangle after = mini.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar X en -3");
        assertEquals(before.y, after.y, "update() no debe modificar Y");
    }

    @Test
    void testDrawDoesNotThrow() {
        // Preparar un Graphics2D de prueba
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        assertDoesNotThrow(() -> mini.draw(g2d), "draw() no debe lanzar excepción");
    }

    @Test
    void testApplyEffectDoesNotThrow() {
        Bird bird = new Bird();
        // Aunque Bird.no tenga makeMini implementado, sólo comprobamos que no explode
        assertDoesNotThrow(() -> mini.applyEffect(bird), "applyEffect() no debe lanzar excepción");
    }

    @Test
    void testGetBoundsSize() {
        Rectangle bounds = mini.getBounds();
        assertEquals(60, bounds.width, "El ancho del hitbox debe ser igual a size");
        assertEquals(60, bounds.height, "La altura del hitbox debe ser igual a size");
    }

    @Test
    void testIsOffScreenTrueWhenCompletelyLeft() {
        // Si x + size < 0 => off screen
        mini = MiniMessi.getInstancia(-61, 0);
        assertTrue(mini.isOffScreen(), "isOffScreen() debe ser true cuando x + size < 0");
    }

    @Test
    void testIsOffScreenFalseWhenVisible() {
        mini = MiniMessi.getInstancia(0, 0);
        assertFalse(mini.isOffScreen(), "isOffScreen() debe ser false cuando el objeto aún es visible");
    }
}
