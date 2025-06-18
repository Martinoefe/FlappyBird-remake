package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GoldenBallTest {

    @BeforeEach
    public void setUp() {
        // Se asegura que antes de cada test el singleton tenga estado conocido
        GoldenBall.getInstancia(0, 0);
    }

    @Test
    public void testGetInstancia_SingletonYPosicion_ViaGetBounds() {
        // Primera llamada: posiciona en (100, 200)
        GoldenBall first = GoldenBall.getInstancia(100, 200);
        Rectangle r1 = first.getBounds();
        assertEquals(100, r1.x, "getInstancia debería fijar x a 100");
        assertEquals(200, r1.y, "getInstancia debería fijar y a 200");
        assertEquals(30, r1.width, "El ancho debe ser size=30");
        assertEquals(30, r1.height, "La altura debe ser size=30");

        // Segunda llamada: al ser singleton, devuelve la misma instancia y actualiza posición
        GoldenBall second = GoldenBall.getInstancia(5, 6);
        assertSame(first, second, "getInstancia debe devolver siempre la misma instancia");
        Rectangle r2 = second.getBounds();
        assertEquals(5, r2.x, "Tras segunda llamada, x debe actualizarse a 5");
        assertEquals(6, r2.y, "Tras segunda llamada, y debe actualizarse a 6");
    }

    @Test
    public void testUpdate_MueveXHaciaIzquierda_ViaGetBounds() {
        GoldenBall gb = GoldenBall.getInstancia(50, 20);
        Rectangle before = gb.getBounds();
        gb.update();
        Rectangle after = gb.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar x en -3");
        assertEquals(before.y, after.y, "update() no debe cambiar y");
    }

    @Test
    public void testApplyEffect_InvocaMakeInvincible() {
        GoldenBall gb = GoldenBall.getInstancia(0, 0);
        Bird birdMock = mock(Bird.class);
        gb.applyEffect(birdMock);
        verify(birdMock, times(1)).makeInvincible(320);
    }

    @Test
    public void testGetBounds_CuadraConXySize() {
        GoldenBall gb = GoldenBall.getInstancia(7, 8);
        Rectangle r = gb.getBounds();
        assertEquals(7, r.x, "getBounds.x debe reflejar la posición x");
        assertEquals(8, r.y, "getBounds.y debe reflejar la posición y");
        assertEquals(30, r.width, "getBounds.width debe ser size=30");
        assertEquals(30, r.height, "getBounds.height debe ser size=30");
    }

    @Test
    public void testIsOffScreen_VerdaderoCuandoSalePantalla() {
        // size = 30, isOffScreen() true cuando x + size <= 0
        GoldenBall gb = GoldenBall.getInstancia(-31, 0);
        assertTrue(gb.isOffScreen(), "Si x + size < 0, isOffScreen debería ser true");

        gb = GoldenBall.getInstancia(-30, 0);
        assertTrue(gb.isOffScreen(), "Si x + size == 0, isOffScreen debería ser true");

        gb = GoldenBall.getInstancia(-29, 0);
        assertFalse(gb.isOffScreen(), "Si x + size > 0, isOffScreen debería ser false");
    }

    @Test
    public void testDraw_InvocaDrawImageConParametrosCorrectos() {
        GoldenBall gb = GoldenBall.getInstancia(12, 34);
        Graphics gMock = mock(Graphics.class);

        gb.draw(gMock);

        Rectangle r = gb.getBounds();
        verify(gMock).drawImage(
                nullable(Image.class), // Acepta imagen null
                eq(r.x), eq(r.y),
                eq(r.width), eq(r.height),
                isNull()
        );
    }
}
