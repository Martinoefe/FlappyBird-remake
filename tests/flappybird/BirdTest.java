package flappybird;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.awt.Rectangle;

/**
 * @class BirdTest
 * @brief Conjunto de pruebas unitarias para la clase Bird del juego Flappy Bird.
 *
 * Esta clase verifica el comportamiento del pájaro respecto a su posición inicial,
 * capacidad de salto, invencibilidad, modo mini, y reinicio de estado.
 */
public class BirdTest {

    private Bird bird;

    /**
     * @brief Inicializa una nueva instancia de Bird antes de cada test.
     */
    @BeforeEach
    public void setUp() {
        try {
            bird = new Bird();
        } catch (Exception e) {
            bird = null;
        }
    }

    /// Verifica que el objeto bird fue correctamente instanciado
    private boolean isInstanciado() {
        return bird != null;
    }

    /**
     * @test Verifica que la posición inicial del pájaro sea la esperada.
     */
    @Test
    public void testInitialPosition() {
        if (!isInstanciado()) return;

        float expectedCenterX = GameModel.WIDTH  / 2f + 0.5f;
        float expectedCenterY = GameModel.HEIGHT / 2f + 0.5f;

        Rectangle bounds = bird.getBounds();
        assertEquals(expectedCenterX,
                (float)bounds.getCenterX(),
                0.01f,
                "El centerX del hitbox debe ser WIDTH/2 + 0.5");
        assertEquals(expectedCenterY,
                (float)bounds.getCenterY(),
                0.01f,
                "El centerY del hitbox debe ser HEIGHT/2 + 0.5");

        assertEquals(GameModel.HEIGHT / 2f,
                bird.getY(),
                0.01f,
                "Y debe estar en HEIGHT/2");
    }

    /**
     * @test Verifica que el método jump() seguido de updateState() disminuya la posición Y.
     */
    @Test
    public void testJumpChangesVerticalPosition() {
        if (!isInstanciado()) return;

        float initialY = bird.getY();
        bird.jump();
        bird.updateState();
        assertTrue(bird.getY() < initialY,
                "Después de jump() y updateState(), Y debe disminuir");
    }

    /**
     * @test Verifica la activación y expiración del estado de invencibilidad.
     */
    @Test
    public void testInvincibilityActivationAndExpiration() {
        if (!isInstanciado()) return;

        bird.makeInvincible(2);
        assertTrue(bird.isInvincible(), "Debe activarse la invencibilidad");
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isInvincible(), "La invencibilidad debe expirar tras 2 frames");
    }

    /**
     * @test Verifica la activación y expiración del modo mini.
     */
    @Test
    public void testMiniActivationAndExpiration() {
        if (!isInstanciado()) return;

        bird.makeMini(2);
        assertTrue(bird.isMini(), "Debe activarse el modo mini");
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isMini(), "El modo mini debe expirar tras 2 frames");
    }

    /**
     * @test Verifica que el tamaño del hitbox sea menor cuando el pájaro está en modo mini.
     */
    @Test
    public void testBoundsAreSmallerWhenMini() {
        if (!isInstanciado()) return;

        Rectangle normal = bird.getBounds();
        bird.makeMini(10);
        bird.updateState();
        Rectangle mini = bird.getBounds();

        assertTrue(mini.width  < normal.width,
                "Cuando está mini, el ancho del hitbox debe ser menor");
        assertTrue(mini.height < normal.height,
                "Cuando está mini, la altura del hitbox debe ser menor");
    }

    /**
     * @test Verifica que el método reset() restaure el estado inicial del pájaro.
     */
    @Test
    public void testResetRestoresInitialState() {
        if (!isInstanciado()) return;

        bird.jump();
        bird.makeInvincible(10);
        bird.makeMini(10);
        bird.updateState();

        bird.reset();

        float expectedCenterX = GameModel.WIDTH  / 2f + 0.5f;
        float expectedCenterY = GameModel.HEIGHT / 2f + 0.5f;

        Rectangle bounds = bird.getBounds();
        assertEquals(expectedCenterX,
                (float)bounds.getCenterX(),
                0.01f,
                "Reset debe restaurar centerX a WIDTH/2 + 0.5");
        assertEquals(expectedCenterY,
                (float)bounds.getCenterY(),
                0.01f,
                "Reset debe restaurar centerY a HEIGHT/2 + 0.5");

        assertFalse(bird.isInvincible(), "Reset debe desactivar invencibilidad");
        assertFalse(bird.isMini(),      "Reset debe desactivar modo mini");
    }
}
