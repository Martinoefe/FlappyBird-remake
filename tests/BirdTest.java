import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flappybird.Bird;

/**
 * Pruebas unitarias para la clase Bird,
 * enfocadas en el comportamiento de invencibilidad.
 */
public class BirdTest {

    private Bird bird;

    /**
     * Se ejecuta antes de cada test para crear un nuevo pájaro.
     */
    @Before
    public void setUp() {
        bird = new Bird();
    }

    /**
     * Verifica que al aplicar makeInvincible con duración > 0,
     * el estado de invencibilidad se activa correctamente.
     */
    @Test
    public void testMakeBirdInvincible_setsStateCorrectly() {
        bird.makeInvincible(240);
        assertTrue(bird.isInvincible());
    }

    /**
     * Verifica que la invencibilidad expira luego del tiempo especificado
     * usando physics() para simular el paso del tiempo (frames).
     */
    @Test
    public void testInvincibilityExpiresAfterDuration() {
        bird.makeInvincible(3); // duración en frames
        bird.physics();
        bird.physics();
        bird.physics(); // debería terminar aca
        assertFalse(bird.isInvincible());
    }
}

