package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class MiniMessiTest
 * @brief Pruebas unitarias para la clase MiniMessi.
 *
 * Verifica el comportamiento singleton, el desplazamiento, los límites de pantalla,
 * el efecto sobre el pájaro y el renderizado.
 */
public class MiniMessiTest {

    private static final int INITIAL_X = 300;
    private static final int INITIAL_Y = 150;
    private MiniMessi mini;

    /**
     * @brief Inicializa la instancia singleton de MiniMessi antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        mini = MiniMessi.getInstancia(INITIAL_X, INITIAL_Y);
    }

    /**
     * @test Verifica que MiniMessi actúe como singleton (una única instancia).
     */
    @Test
    void testSingletonIdentity() {
        MiniMessi other = MiniMessi.getInstancia(10, 20);
        assertSame(mini, other, "MiniMessi debe comportarse como singleton");
    }

    /**
     * @test Verifica que la posición se actualice correctamente tras llamar a getInstancia().
     */
    @Test
    void testPositionAfterGetInstancia() {
        Rectangle bounds = mini.getBounds();
        assertEquals(INITIAL_X, bounds.x, "La coordenada X debe coincidir con la pasada");
        assertEquals(INITIAL_Y, bounds.y, "La coordenada Y debe coincidir con la pasada");
    }

    /**
     * @test Verifica que update() desplace el objeto 3 unidades a la izquierda.
     */
    @Test
    void testUpdateMovesLeftByThree() {
        Rectangle before = mini.getBounds();
        mini.update();
        Rectangle after = mini.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar X en -3");
        assertEquals(before.y, after.y, "update() no debe modificar Y");
    }

    /**
     * @test Verifica que el método draw() no arroje errores al ejecutarse.
     */
    @Test
    void testDrawDoesNotThrow() {
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        assertDoesNotThrow(() -> mini.draw(g2d), "draw() no debe lanzar excepción");
    }

    /**
     * @test Verifica que applyEffect() no arroje excepciones al ejecutarse sobre un pájaro.
     */
    @Test
    void testApplyEffectDoesNotThrow() {
        Bird bird = new Bird();
        assertDoesNotThrow(() -> mini.applyEffect(bird), "applyEffect() no debe lanzar excepción");
    }

    /**
     * @test Verifica que las dimensiones del hitbox sean 60x60 píxeles.
     */
    @Test
    void testGetBoundsSize() {
        Rectangle bounds = mini.getBounds();
        assertEquals(60, bounds.width, "El ancho del hitbox debe ser igual a size");
        assertEquals(60, bounds.height, "La altura del hitbox debe ser igual a size");
    }

    /**
     * @test Verifica que isOffScreen() devuelva true cuando el objeto está completamente fuera de pantalla.
     */
    @Test
    void testIsOffScreenTrueWhenCompletelyLeft() {
        mini = MiniMessi.getInstancia(-61, 0);
        assertTrue(mini.isOffScreen(), "isOffScreen() debe ser true cuando x + size < 0");
    }

    /**
     * @test Verifica que isOffScreen() devuelva false si el objeto aún es visible.
     */
    @Test
    void testIsOffScreenFalseWhenVisible() {
        mini = MiniMessi.getInstancia(0, 0);
        assertFalse(mini.isOffScreen(), "isOffScreen() debe ser false cuando el objeto aún es visible");
    }
}
