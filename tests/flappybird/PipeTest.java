package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class PipeTest
 * @brief Pruebas unitarias para la clase Pipe del juego Flappy Bird.
 *
 * Se testea el comportamiento de las tuberías (superiores e inferiores): movimiento,
 * límites de colisión, visibilidad en pantalla y renderizado.
 */
public class PipeTest {

    private static final int START_X = 100;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 200;

    private BufferedImage dummyHead;
    private BufferedImage dummyBody;
    private Pipe topPipe;
    private Pipe bottomPipe;

    /**
     * @brief Crea imágenes de prueba y tuberías (superior e inferior) antes de cada test.
     */
    @BeforeEach
    void setUp() {
        dummyHead = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        dummyBody = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        topPipe = new Pipe(START_X, WIDTH, HEIGHT, true, dummyHead, dummyBody);
        bottomPipe = new Pipe(START_X, WIDTH, HEIGHT, false, dummyHead, dummyBody);
    }

    /**
     * @test Verifica que updateState() desplace la tubería 3 unidades hacia la izquierda.
     */
    @Test
    void testUpdateStateMovesLeftByThree() {
        float initialX = topPipe.getX();
        topPipe.updateState();
        assertEquals(initialX - 3, topPipe.getX(), 0.001,
                "updateState() debe reducir x en 3");
    }

    /**
     * @test Verifica que getX() refleje la posición horizontal inicial de la tubería.
     */
    @Test
    void testGetXReflectsPosition() {
        assertEquals(START_X, topPipe.getX(), 0.001);
    }

    /**
     * @test Verifica que getBounds() para la tubería superior tenga la posición y tamaño correcto.
     */
    @Test
    void testGetBoundsTop() {
        Rectangle bounds = topPipe.getBounds();
        assertEquals(START_X, bounds.x);
        assertEquals(0, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);
    }

    /**
     * @test Verifica que getBounds() para la tubería inferior tenga la posición y tamaño correcto.
     */
    @Test
    void testGetBoundsBottom() {
        Rectangle bounds = bottomPipe.getBounds();
        int expectedY = GameModel.HEIGHT - HEIGHT;
        assertEquals(START_X, bounds.x);
        assertEquals(expectedY, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);
    }

    /**
     * @test Verifica que isOffScreen() devuelva true cuando la tubería ya no está visible.
     */
    @Test
    void testIsOffScreenTrueWhenLeft() {
        Pipe off = new Pipe(-WIDTH - 1, WIDTH, HEIGHT, true, dummyHead, dummyBody);
        assertTrue(off.isOffScreen(), "isOffScreen() debe ser true cuando x + width < 0");
    }

    /**
     * @test Verifica que isOffScreen() devuelva false si la tubería aún está en pantalla.
     */
    @Test
    void testIsOffScreenFalseWhenVisible() {
        assertFalse(topPipe.isOffScreen(),
                "isOffScreen() debe ser false cuando la tubería aún es visible");
    }

    /**
     * @test Verifica que el método draw() se ejecute sin arrojar excepciones.
     */
    @Test
    void testDrawDoesNotThrow() {
        BufferedImage canvas = new BufferedImage(800, GameModel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();
        assertDoesNotThrow(() -> {
            topPipe.draw(g2d);
            bottomPipe.draw(g2d);
        }, "draw() no debe lanzar ninguna excepción");
    }
}
