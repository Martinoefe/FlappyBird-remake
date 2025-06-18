package flappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

public class BirdTest {
    private Bird bird;

    @BeforeEach
    public void setUp() {
        bird = new Bird();
    }

    @Test
    public void testInitialPosition() {
        // Debido al redondeo en ambos ejes, el centro X/Y del hitbox queda en mitad + 0.5
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

        // bird.getY() sigue siendo HEIGHT/2 exacto
        assertEquals(GameModel.HEIGHT / 2f,
                bird.getY(),
                0.01f,
                "Y debe estar en HEIGHT/2");
    }

    @Test
    public void testJumpChangesVerticalPosition() {
        float initialY = bird.getY();
        bird.jump();
        bird.updateState();
        assertTrue(bird.getY() < initialY,
                "Después de jump() y updateState(), Y debe disminuir");
    }

    @Test
    public void testInvincibilityActivationAndExpiration() {
        bird.makeInvincible(2);
        assertTrue(bird.isInvincible(), "Debe activarse la invencibilidad");
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isInvincible(), "La invencibilidad debe expirar tras 2 frames");
    }

    @Test
    public void testMiniActivationAndExpiration() {
        bird.makeMini(2);
        assertTrue(bird.isMini(), "Debe activarse el modo mini");
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isMini(), "El modo mini debe expirar tras 2 frames");
    }

    @Test
    public void testBoundsAreSmallerWhenMini() {
        Rectangle normal = bird.getBounds();
        bird.makeMini(10);
        bird.updateState();
        Rectangle mini = bird.getBounds();

        assertTrue(mini.width  < normal.width,
                "Cuando está mini, el ancho del hitbox debe ser menor");
        assertTrue(mini.height < normal.height,
                "Cuando está mini, la altura del hitbox debe ser menor");
    }

    @Test
    public void testResetRestoresInitialState() {
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
