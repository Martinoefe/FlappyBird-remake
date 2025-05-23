import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flappybird.Pipe;
import java.awt.Rectangle;

public class PipeTest {

    private Pipe pipe;

    /**
     * Inicializa una tubería antes de cada test.
     */
    @Before
    public void setUp() {
        pipe = new Pipe(300, 50, 200, true, null, null); // sin imágenes
    }

    /**
     * Verifica que la tubería se inicializa en la posición correcta.
     */
    @Test
    public void testPipeInitialization() {
        assertEquals(300, pipe.getX());
        assertEquals(200, pipe.getHeight());
    }

    /**
     * Verifica que la tubería se mueve hacia la izquierda al actualizarse.
     */
    @Test
    public void testPipeMovesLeft() {
        int initialX = pipe.getX();
        pipe.update();
        assertTrue(pipe.getX() < initialX);
    }

    /**
     * Verifica que los límites de colisión estén bien definidos.
     */
    @Test
    public void testPipeBoundsNotNull() {
        Rectangle bounds = pipe.getBounds();
        assertNotNull(bounds);
    }
}
