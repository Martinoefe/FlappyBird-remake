package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class GoldenBallTest {

    private static final int INITIAL_X = 400;
    private static final int INITIAL_Y = 200;
    private GoldenBall ball;

    @BeforeEach
    public void setUp() {
        // Siempre obtenemos la misma instancia y la posicionamos
        ball = GoldenBall.getInstancia(INITIAL_X, INITIAL_Y);
    }

    @Test
    public void testSingletonIdentity() {
        GoldenBall another = GoldenBall.getInstancia(123, 456);
        // Debe ser la misma instancia
        assertSame(ball, another, "GoldenBall debe ser un singleton");
    }

    @Test
    public void testPositionAfterGetInstancia() {
        // La llamada al factory actualiza x,y
        assertEquals(INITIAL_X, ball.getBounds().x, "X debe coincidir con el valor pasado");
        assertEquals(INITIAL_Y, ball.getBounds().y, "Y debe coincidir con el valor pasado");
    }

    @Test
    public void testUpdateMovesLeft() {
        Rectangle before = ball.getBounds();
        ball.update();
        Rectangle after = ball.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar x en -3");
        // Y no cambia
        assertEquals(before.y, after.y, "update() no debe cambiar y");
    }

    @Test
    public void testDrawDoesNotThrow() {
        // Preparamos un Graphics2D de prueba
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        assertDoesNotThrow(() -> ball.draw(g2d), "draw() no debe lanzar excepción");
    }

    @Test
    public void testApplyEffectMakesBirdInvincible() {
        Bird bird = new Bird();
        assertFalse(bird.isInvincible(), "Inicialmente no debe ser invencible");
        ball.applyEffect(bird);
        assertTrue(bird.isInvincible(), "applyEffect() debe activar invencibilidad");
    }

    @Test
    public void testGetBoundsSize() {
        Rectangle bounds = ball.getBounds();
        assertEquals(30, bounds.width, "El ancho del hitbox debe ser igual a size");
        assertEquals(30, bounds.height, "La altura del hitbox debe ser igual a size");
    }

    @Test
    public void testIsOffScreenTrueWhenCompletelyLeft() {
        // Ponemos x suficientemente pequeño
        ball = GoldenBall.getInstancia(-31, 0);
        assertTrue(ball.isOffScreen(), "isOffScreen() debe ser true cuando x + size <= 0");
    }

    @Test
    public void testIsOffScreenFalseWhenVisible() {
        ball = GoldenBall.getInstancia(0, 0);
        assertFalse(ball.isOffScreen(), "isOffScreen() debe ser false cuando aún está visible");
    }
}
