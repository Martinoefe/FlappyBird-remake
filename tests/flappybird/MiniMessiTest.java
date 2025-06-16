package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import org.junit.jupiter.api.Test;

public class MiniMessiTest {

    @Test
    public void testGetInstancia_SingletonYPosicion_ViaGetBounds() {
        // Primera llamada: posiciona en (100, 200)
        MiniMessi first = MiniMessi.getInstancia(100, 200);
        Rectangle r1 = first.getBounds();
        assertEquals(100, r1.x, "getInstancia debería fijar x a 100");
        assertEquals(200, r1.y, "getInstancia debería fijar y a 200");
        assertEquals(60, r1.width, "El ancho debe ser size=60");
        assertEquals(60, r1.height, "La altura debe ser size=60");

        // Segunda llamada: al ser singleton, devuelve la misma instancia y actualiza posición
        MiniMessi second = MiniMessi.getInstancia(5, 6);
        assertSame(first, second, "getInstancia debe devolver siempre la misma instancia");
        Rectangle r2 = second.getBounds();
        assertEquals(5, r2.x, "Tras segunda llamada, x debe actualizarse a 5");
        assertEquals(6, r2.y, "Tras segunda llamada, y debe actualizarse a 6");
    }

    @Test
    public void testUpdate_MueveXHaciaIzquierda_ViaGetBounds() {
        MiniMessi mm = MiniMessi.getInstancia(50, 20);
        Rectangle before = mm.getBounds();
        mm.update();
        Rectangle after = mm.getBounds();
        assertEquals(before.x - 3, after.x, "update() debe desplazar x en -3");
        assertEquals(before.y, after.y, "update() no debe cambiar y");
    }

    @Test
    public void testApplyEffect_InvocaMakeMini() {
        MiniMessi mm = MiniMessi.getInstancia(0, 0);
        Bird birdMock = mock(Bird.class);
        mm.applyEffect(birdMock);
        verify(birdMock, times(1)).makeMini(400);
    }

    @Test
    public void testGetBounds_CuadraConXySize() {
        MiniMessi mm = MiniMessi.getInstancia(7, 8);
        Rectangle r = mm.getBounds();
        assertEquals(7, r.x, "getBounds.x debe reflejar la posición x");
        assertEquals(8, r.y, "getBounds.y debe reflejar la posición y");
        assertEquals(60, r.width, "getBounds.width debe ser size=60");
        assertEquals(60, r.height, "getBounds.height debe ser size=60");
    }

    @Test
    public void testIsOffScreen_VerdaderoCuandoSalePantalla() {
        // size = 60, isOffScreen() true cuando x + size < 0
        MiniMessi mm;

        mm = MiniMessi.getInstancia(-61, 0);
        assertTrue(mm.isOffScreen(), "Si x + size < 0 (por ejemplo -61+60=-1), isOffScreen debería ser true");

        mm = MiniMessi.getInstancia(-60, 0);
        assertFalse(mm.isOffScreen(), "Si x + size == 0 (por ejemplo -60+60=0), isOffScreen debería ser false");

        mm = MiniMessi.getInstancia(-59, 0);
        assertFalse(mm.isOffScreen(), "Si x + size > 0 (por ejemplo -59+60=1), isOffScreen debería ser false");
    }

    @Test
    public void testDraw_InvocaDrawImageConParametrosCorrectos() throws Exception {
        MiniMessi mm = MiniMessi.getInstancia(12, 34);
        Graphics gMock = mock(Graphics.class);

        // Llamamos draw: interesa que invoque drawImage con los parámetros esperados.
        mm.draw(gMock);

        Rectangle r = mm.getBounds();
        // Usamos any(Image.class) por si la imagen carga null ó no; Mockito any() acepta null en la verificación.
        verify(gMock, times(1)).drawImage(any(Image.class), eq(r.x), eq(r.y), eq(r.width), eq(r.height), eq(null));
    }
}
