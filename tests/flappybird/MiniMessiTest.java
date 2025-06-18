package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MiniMessiTest {

    @BeforeEach
    public void setUp() {
        // Resetea posición del singleton a un estado conocido
        MiniMessi.getInstancia(0, 0);
    }

    @Test
    public void testGetInstancia_SingletonYPosicion() {
        MiniMessi first = MiniMessi.getInstancia(100, 200);
        Rectangle r1 = first.getBounds();
        assertEquals(100, r1.x);
        assertEquals(200, r1.y);
        assertEquals(60, r1.width);
        assertEquals(60, r1.height);

        MiniMessi second = MiniMessi.getInstancia(5, 6);
        assertSame(first, second, "Debe ser la misma instancia (singleton)");
        Rectangle r2 = second.getBounds();
        assertEquals(5, r2.x);
        assertEquals(6, r2.y);
    }

    @Test
    public void testUpdate_DesplazaXEnMenosTres() {
        MiniMessi mm = MiniMessi.getInstancia(50, 20);
        int xAntes = mm.getBounds().x;
        mm.update();
        int xDespues = mm.getBounds().x;
        assertEquals(xAntes - 3, xDespues);
    }

    @Test
    public void testApplyEffect_LlamaMakeMini() {
        MiniMessi mm = MiniMessi.getInstancia(0, 0);
        Bird birdMock = mock(Bird.class);
        mm.applyEffect(birdMock);
        verify(birdMock).makeMini(400);
    }

    @Test
    public void testGetBounds_Correcto() {
        MiniMessi mm = MiniMessi.getInstancia(7, 8);
        Rectangle r = mm.getBounds();
        assertEquals(7, r.x);
        assertEquals(8, r.y);
        assertEquals(60, r.width);
        assertEquals(60, r.height);
    }

    @Test
    public void testIsOffScreen() {
        MiniMessi mm = MiniMessi.getInstancia(-61, 0);
        assertTrue(mm.isOffScreen());

        mm = MiniMessi.getInstancia(-60, 0);
        assertFalse(mm.isOffScreen());

        mm = MiniMessi.getInstancia(-59, 0);
        assertFalse(mm.isOffScreen());
    }

    @Test
    public void testDraw_LlamaDrawImageConParametrosCorrectos() {
        MiniMessi mm = MiniMessi.getInstancia(12, 34);
        Graphics gMock = mock(Graphics.class);

        mm.draw(gMock);

        Rectangle r = mm.getBounds();
        verify(gMock).drawImage(
                nullable(Image.class),   // permite null
                eq(r.x), eq(r.y),
                eq(r.width), eq(r.height),
                isNull()
        );
    }
}
