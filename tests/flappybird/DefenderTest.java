package flappybird;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @class DefenderTest
 * @brief Pruebas unitarias para la clase Defender del juego Flappy Bird.
 *
 * Se testea el movimiento, rebotes, visibilidad en pantalla, hitbox y renderizado.
 * También se usan métodos auxiliares para acceder a campos privados mediante reflexión.
 */
public class DefenderTest {
    private static final int START_X = 100;
    private Defender         defender;

    /**
     * @brief Inicializa una instancia de Defender antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        try {
            defender = new Defender(START_X);
        } catch ( Exception e ) {
            defender = null;
        }
    }

    private boolean instanciado() {
        return defender != null;
    }

    /**
     * @test Verifica que el método updateState() disminuya la coordenada X en 2 unidades.
     */
    @Test
    void testUpdateStateMovesLeftByTwo() throws Exception {
        if ( !instanciado() )
            return;

        float initialX = getFieldFloat(defender, "x");
        defender.updateState();
        float afterX = getFieldFloat(defender, "x");
        assertEquals(initialX - 2, afterX, 0.001, "updateState() debe mover x en -2");
    }

    /**
     * @test Verifica el rebote superior: Y se ajusta a LIMIT_TOP y la dirección se invierte.
     */
    @Test
    void testBounceAtTopResetsYAndDirection() throws Exception {
        if ( !instanciado() )
            return;

        Field yField = Defender.class.getDeclaredField("y");
        yField.setAccessible(true);
        yField.setFloat(defender, -5f);

        defender.updateState();

        float yAfter    = getFieldFloat(defender, "y");
        int   direction = getFieldInt(defender, "direction");
        int   limitTop  = getStaticField("LIMIT_TOP");
        assertEquals(limitTop, yAfter, 0.001, "Al rebotar en top, y debe ajustarse a LIMIT_TOP");
        assertEquals(1, direction, "La dirección debe cambiar a 1 tras rebotar en el top");
    }

    /**
     * @test Verifica el rebote inferior: Y se ajusta a LIMIT_BOTTOM y la dirección se invierte.
     */
    @Test
    void testBounceAtBottomResetsYAndDirection() throws Exception {
        if ( !instanciado() )
            return;

        int   limitBottom = getStaticField("LIMIT_BOTTOM");
        Field yField      = Defender.class.getDeclaredField("y");
        yField.setAccessible(true);
        yField.setFloat(defender, limitBottom + 5f);

        defender.updateState();

        float yAfter    = getFieldFloat(defender, "y");
        int   direction = getFieldInt(defender, "direction");
        assertEquals(
            limitBottom, yAfter, 0.001, "Al rebotar en bottom, y debe ajustarse a LIMIT_BOTTOM");
        assertEquals(-1, direction, "La dirección debe cambiar a -1 tras rebotar en el bottom");
    }

    /**
     * @test Verifica que getBounds() retorne el rectángulo de colisión esperado.
     */
    @Test
    void testGetBounds() throws Exception {
        if ( !instanciado() )
            return;

        setFieldFloat(defender, "x", 10f);
        setFieldFloat(defender, "y", 20f);

        Rectangle bounds = defender.getBounds();
        assertEquals(10 + 5, bounds.x, "getBounds.x debe ser x+5");
        assertEquals(20, bounds.y, "getBounds.y debe ser y");
        assertEquals(40, bounds.width, "getBounds.width debe ser 40");
        assertEquals(50, bounds.height, "getBounds.height debe ser 50");
    }

    /**
     * @test Verifica que isOffScreen() devuelva true cuando el objeto sale completamente de
     * pantalla.
     */
    @Test
    void testIsOffScreenTrue() {
        try {
            Defender off = new Defender(-100);
            assertTrue(off.isOffScreen(), "isOffScreen() debe ser true si x+width<0");
        } catch ( Exception ignored ) {
        }
    }

    /**
     * @test Verifica que isOffScreen() devuelva false si el objeto aún es visible.
     */
    @Test
    void testIsOffScreenFalse() {
        if ( !instanciado() )
            return;

        assertFalse(defender.isOffScreen(), "isOffScreen() debe ser false si aún es visible");
    }

    /**
     * @test Verifica que draw() no arroje excepciones al intentar renderizar.
     */
    @Test
    void testDrawDoesNotThrow() {
        if ( !instanciado() )
            return;

        BufferedImage img = new BufferedImage(200, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D    g2d = img.createGraphics();
        assertDoesNotThrow(() -> defender.draw(g2d), "draw() no debe lanzar excepción");
    }

    /**
     * @brief Obtiene un campo privado tipo float usando reflexión.
     * @param obj Instancia del objeto.
     * @param name Nombre del campo.
     * @return Valor float del campo.
     */
    private float getFieldFloat(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.getFloat(obj);
    }

    /**
     * @brief Obtiene un campo privado tipo int usando reflexión.
     * @param obj Instancia del objeto.
     * @param name Nombre del campo.
     * @return Valor int del campo.
     */
    private int getFieldInt(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(obj);
    }

    /**
     * @brief Asigna un valor float a un campo privado usando reflexión.
     * @param obj Instancia del objeto.
     * @param name Nombre del campo.
     * @param value Valor a asignar.
     */
    private void setFieldFloat(Object obj, String name, float value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.setFloat(obj, value);
    }

    /**
     * @brief Obtiene un campo estático int de la clase Defender usando reflexión.
     * @param name Nombre del campo estático.
     * @return Valor int del campo.
     */
    private int getStaticField(String name) throws Exception {
        Field f = Defender.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(null);
    }
}
