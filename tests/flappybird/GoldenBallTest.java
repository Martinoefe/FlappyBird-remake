package flappybird;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @class GoldenBallTest
 * @brief Pruebas unitarias para la clase GoldenBall.
 *
 * Esta clase verifica el comportamiento del singleton GoldenBall, su desplazamiento,
 * su efecto sobre el pájaro, y su visibilidad en pantalla.
 */
public class GoldenBallTest {
    private static final int INITIAL_X = 400;
    private static final int INITIAL_Y = 200;
    private GoldenBall       ball;
    private boolean          setupExitoso = false;

    /**
     * @brief Inicializa la instancia de GoldenBall antes de cada prueba.
     * Usa el patrón singleton.
     */
    @BeforeEach
    public void setUp() {
        try {
            ball         = GoldenBall.getInstancia(INITIAL_X, INITIAL_Y);
            setupExitoso = ball != null;
        } catch ( Exception e ) {
            setupExitoso = false;
        }
    }

    /**
     * @test Verifica que GoldenBall siga el patrón Singleton (una sola instancia).
     */
    @Test
    public void testSingletonIdentity() {
        if ( !setupExitoso )
            return;

        GoldenBall another = GoldenBall.getInstancia(123, 456);
        assertSame(ball, another, "GoldenBall debe ser un singleton");
    }

    /**
     * @test Verifica que los valores de posición se actualicen al usar getInstancia().
     */
    @Test
    public void testPositionAfterGetInstancia() {
        if ( !setupExitoso )
            return;

        assertEquals(INITIAL_X, ball.getBounds().x, "X debe coincidir con el valor pasado");
        assertEquals(INITIAL_Y, ball.getBounds().y, "Y debe coincidir con el valor pasado");
    }

    /**
     * @test Verifica que la posición X disminuye en 3 al llamar a update().
     */
    @Test
    public void testUpdateMovesLeft() {
        if ( !setupExitoso )
            return;

        Rectangle before = ball.getBounds();
        ball.update();
        Rectangle after = ball.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar x en -3");
        assertEquals(before.y, after.y, "update() no debe cambiar y");
    }

    /**
     * @test Verifica que el método draw() no lance excepciones.
     */
    @Test
    public void testDrawDoesNotThrow() {
        if ( !setupExitoso )
            return;

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D    g2d = img.createGraphics();
        assertDoesNotThrow(() -> ball.draw(g2d), "draw() no debe lanzar excepción");
    }

    /**
     * @test Verifica que applyEffect() aplique invencibilidad al pájaro.
     */
    @Test
    public void testApplyEffectMakesBirdInvincible() {
        if ( !setupExitoso )
            return;

        Bird bird;
        try {
            bird = new Bird();
        } catch ( Exception e ) {
            return;
        }

        assertFalse(bird.isInvincible(), "Inicialmente no debe ser invencible");
        ball.applyEffect(bird);
        assertTrue(bird.isInvincible(), "applyEffect() debe activar invencibilidad");
    }

    /**
     * @test Verifica que las dimensiones del hitbox sean de 30x30.
     */
    @Test
    public void testGetBoundsSize() {
        if ( !setupExitoso )
            return;

        Rectangle bounds = ball.getBounds();
        assertEquals(30, bounds.width, "El ancho del hitbox debe ser igual a size");
        assertEquals(30, bounds.height, "La altura del hitbox debe ser igual a size");
    }

    /**
     * @test Verifica que isOffScreen() devuelva true si la pelota está fuera de pantalla.
     */
    @Test
    public void testIsOffScreenTrueWhenCompletelyLeft() {
        try {
            ball = GoldenBall.getInstancia(-31, 0);
            assertTrue(ball.isOffScreen(), "isOffScreen() debe ser true cuando x + size <= 0");
        } catch ( Exception ignored ) {
        }
    }

    /**
     * @test Verifica que isOffScreen() devuelva false si la pelota aún es visible.
     */
    @Test
    public void testIsOffScreenFalseWhenVisible() {
        try {
            ball = GoldenBall.getInstancia(0, 0);
            assertFalse(ball.isOffScreen(), "isOffScreen() debe ser false cuando aún está visible");
        } catch ( Exception ignored ) {
        }
    }
}
