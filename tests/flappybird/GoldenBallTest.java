package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GoldenBallTest {

    // Helpers de reflexión para acceder a campos privados de GoldenBall
    private Field fieldX;
    private Field fieldY;
    private Field fieldActive;
    private Field fieldActivationTime;
    private Field fieldImg;
    private Field fieldSize;

    @BeforeEach
    public void setUpReflection() throws Exception {
        Class<GoldenBall> cls = GoldenBall.class;
        fieldX = cls.getDeclaredField("x");
        fieldX.setAccessible(true);
        fieldY = cls.getDeclaredField("y");
        fieldY.setAccessible(true);
        fieldActive = cls.getDeclaredField("active");
        fieldActive.setAccessible(true);
        fieldActivationTime = cls.getDeclaredField("activationTime");
        fieldActivationTime.setAccessible(true);
        fieldImg = cls.getDeclaredField("img");
        fieldImg.setAccessible(true);
        fieldSize = cls.getDeclaredField("size");
        fieldSize.setAccessible(true);

        // Antes de cada prueba, es conveniente "resetear" el singleton.
        // Dado que GoldenBall usa singleton, hay un único objeto. 
        // Llamamos a getInstancia con coordenadas 0,0 para reiniciar estado
        GoldenBall.getInstancia(0, 0);
    }

    @Test
    public void testGetInstancia_SingletonYPosicion() throws Exception {
        GoldenBall first = GoldenBall.getInstancia(100, 200);

        // Verificamos que x y y hayan quedado establecidos
        int x = fieldX.getInt(first);
        int y = fieldY.getInt(first);
        assertEquals(100, x, "El campo x debe establecerse a 100");
        assertEquals(200, y, "El campo y debe establecerse a 200");

        // El flag active debe haberse reseteado a false
        boolean active = fieldActive.getBoolean(first);
        assertFalse(active, "active debe ser false tras getInstancia");

        // Al ser singleton, otra llamada devuelve la misma instancia
        GoldenBall second = GoldenBall.getInstancia(5, 6);
        assertSame(first, second, "getInstancia debe devolver siempre la misma instancia");
        // Y actualiza x,y:
        assertEquals(5, fieldX.getInt(second));
        assertEquals(6, fieldY.getInt(second));
    }

    @Test
    public void testUpdate_MueveXHaciaIzquierda() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(50, 0);
        int beforeX = fieldX.getInt(gb);
        gb.update();
        int afterX = fieldX.getInt(gb);
        assertEquals(beforeX - 3, afterX, "update() debe restar 3 al campo x");
    }

    @Test
    public void testUpdate_DesactivaTrasTiempo() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(0, 0);

        // Simulamos que estuvo activo hace más de 5 segundos
        fieldActive.setBoolean(gb, true);
        long pastTime = System.currentTimeMillis() - 6000;
        fieldActivationTime.setLong(gb, pastTime);

        gb.update();

        boolean activePost = fieldActive.getBoolean(gb);
        assertFalse(activePost, "Después de más de 5s desde activationTime, update() debe poner active=false");
    }

    @Test
    public void testApplyEffect_InvocaMakeInvincibleYActiva() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(0, 0);

        // Mockeamos Bird
        Bird birdMock = mock(Bird.class);

        // Ejecutamos applyEffect
        gb.applyEffect(birdMock);

        // Verificamos que bird.makeInvincible(320) fue llamado
        verify(birdMock, times(1)).makeInvincible(320);

        // Verificamos que active = true
        assertTrue(fieldActive.getBoolean(gb), "applyEffect debe setear active=true");

        // Verificamos que activationTime se actualizó a aproximadamente ahora
        long activationTime = fieldActivationTime.getLong(gb);
        long diff = System.currentTimeMillis() - activationTime;
        assertTrue(diff >= 0 && diff < 500, "activationTime debe ser cercano al momento de la llamada");
    }

    @Test
    public void testGetBounds_CuadraConXySize() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(7, 8);
        // Aseguramos size: campo final int size = 30
        int size = fieldSize.getInt(gb);
        Rectangle r = gb.getBounds();
        assertEquals(7, r.x);
        assertEquals(8, r.y);
        assertEquals(size, r.width);
        assertEquals(size, r.height);
    }

    @Test
    public void testIsOffScreen_VerdaderoCuandoXMasSizeMenorOIgualCero() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(0, 0);
        int size = fieldSize.getInt(gb);

        // Caso justo: x + size == 0
        fieldX.setInt(gb, -size);
        assertTrue(gb.isOffScreen(), "Si x + size == 0, isOffScreen debe ser true");

        // Caso menor: x + size < 0
        fieldX.setInt(gb, -size - 1);
        assertTrue(gb.isOffScreen(), "Si x + size < 0, isOffScreen debe ser true");

        // Caso mayor: x + size > 0
        fieldX.setInt(gb, -size + 1);
        assertFalse(gb.isOffScreen(), "Si x + size > 0, isOffScreen debe ser false");
    }

    @Test
    public void testDraw_InvocaDrawImageConParametrosCorrectos() throws Exception {
        GoldenBall gb = GoldenBall.getInstancia(12, 34);
        // Preparamos un Image dummy y lo inyectamos en el campo img
        Image dummy = mock(Image.class);
        fieldImg.set(gb, dummy);
        Graphics gMock = mock(Graphics.class);

        // Obtenemos x,y,size para la verificación
        int x = fieldX.getInt(gb);
        int y = fieldY.getInt(gb);
        int size = fieldSize.getInt(gb);

        // Llamamos draw
        gb.draw(gMock);

        // Verificamos que drawImage haya sido invocado con (img, x, y, size, size, null)
        verify(gMock, times(1)).drawImage(dummy, x, y, size, size, null);
    }

    @Test
    public void testImageLoadIOException_NoFalla_GetInstancia() throws Exception {
        // Este test verifica que, si no existe la imagen, getInstancia no lanza excepción no capturada.
        // Para ello, podemos renombrar temporalmente o apuntar a una ruta que no exista.
        // Pero dado que la clase internamente hace ImageIO.read(new File("images/balon_de_oro.png")) y captura IOException,
        // simplemente llamamos getInstancia con ruta inexistente (que está hardcodeada) y verificamos que no lanza.
        // Ya en setUpReflection llamamos getInstancia, así que aquí solo verificamos que no lanza.
        assertDoesNotThrow(() -> {
            GoldenBall.getInstancia(123, 456);
        }, "getInstancia debe capturar IOException internamente y no propagarla");
    }
}
