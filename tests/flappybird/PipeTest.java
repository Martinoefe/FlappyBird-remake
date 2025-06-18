package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PipeTest {

    private Image mockHeadImg;
    private Image mockBodyImg;

    @BeforeEach
    public void setUp() {
        mockHeadImg = mock(Image.class);
        mockBodyImg = mock(Image.class);
    }

    @Test
    public void testConstructorSuperior_PosicionamientoCorrecto() {
        Pipe pipe = new Pipe(100, 50, 120, true, mockHeadImg, mockBodyImg);
        Rectangle bounds = pipe.getBounds();

        assertEquals(100, bounds.x);
        assertEquals(0, bounds.y);
        assertEquals(50, bounds.width);
        assertEquals(120, bounds.height);
    }

    @Test
    public void testConstructorInferior_PosicionamientoCorrecto() {
        Pipe pipe = new Pipe(200, 60, 150, false, mockHeadImg, mockBodyImg);
        Rectangle bounds = pipe.getBounds();

        assertEquals(200, bounds.x);
        assertEquals(GameModel.HEIGHT - 150, bounds.y);
        assertEquals(60, bounds.width);
        assertEquals(150, bounds.height);
    }

    @Test
    public void testUpdateState_DesplazaX() {
        Pipe pipe = new Pipe(300, 40, 100, true, mockHeadImg, mockBodyImg);
        float xAntes = pipe.getX();
        pipe.updateState();
        float xDespues = pipe.getX();

        assertEquals(xAntes - 3, xDespues, 0.01f);
    }

    @Test
    public void testIsOffScreen_TrueCuandoSale() {
        Pipe pipe = new Pipe(-41, 40, 100, true, mockHeadImg, mockBodyImg);
        assertTrue(pipe.isOffScreen(), "x + width < 0 debería ser true");

        pipe = new Pipe(-39, 40, 100, true, mockHeadImg, mockBodyImg);
        assertFalse(pipe.isOffScreen(), "x + width > 0 debería ser false");
    }

    @Test
    public void testDraw_Superior_InvocaDrawImage() {
        Pipe pipe = new Pipe(100, 50, 120, true, mockHeadImg, mockBodyImg);
        Graphics gMock = mock(Graphics.class);

        pipe.draw(gMock);

        verify(gMock).drawImage(eq(mockBodyImg), anyInt(), eq(0), eq(50), eq(120), isNull());
        verify(gMock).drawImage(eq(mockHeadImg), anyInt(), eq(110), eq(60), eq(20), isNull());
    }

    @Test
    public void testDraw_Inferior_InvocaDrawImage() {
        Pipe pipe = new Pipe(100, 50, 150, false, mockHeadImg, mockBodyImg);
        Graphics gMock = mock(Graphics.class);

        pipe.draw(gMock);

        int expectedY = GameModel.HEIGHT - 150;
        verify(gMock).drawImage(eq(mockBodyImg), anyInt(), eq(expectedY), eq(50), eq(150), isNull());
        verify(gMock).drawImage(eq(mockHeadImg), anyInt(), eq(expectedY), eq(60), eq(20), isNull());
    }
}
