import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flappybird.Defender;

/**
 * Pruebas unitarias para la clase Defender,
 * verificando comportamiento de movimiento y límites.
 */
public class DefenderTest {

    private Defender defender;

    /**
     * Crea un defensor con posición inicial cerca del borde derecho.
     */
    @Before
    public void setUp() {
        defender = new Defender(100);
    }

    /**
     * Verifica que el defensor se mueva verticalmente dentro de límites.
     */
    @Test
    public void testVerticalMovementWithinBounds() {
        int alturaJuego = 600;
        int altoDefensor = 50;

        for (int i = 0; i < 300; i++) {
            defender.update();
            int y = defender.getY();
            assertTrue("El defensor se salió por arriba", y >= 0);
            assertTrue("El defensor se salió por abajo", y <= alturaJuego - altoDefensor);
        }
    }

    /**
     * Verifica que el defensor eventualmente sale de pantalla por la izquierda.
     */
    @Test
    public void testDefenderOffScreen() {
        Defender d = new Defender(100);
        for (int i = 0; i < 100; i++) {
            d.update();
        }
        assertTrue("El defensor no salió de pantalla después de 100 updates", d.isOffScreen());
    }

    /**
     * Verifica que la colisión se representa con un Rectangle.
     */
    @Test
    public void testGetBoundsNotNull() {
        assertNotNull(defender.getBounds());
    }
}
