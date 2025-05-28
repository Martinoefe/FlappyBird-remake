import flappybird.Bird;
import flappybird.FlappyBird;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BirdTest {

    private Bird bird;

    @BeforeEach
    public void setUp() {
        bird = new Bird();
    }

    @Test
    public void testInitialPosition() {
        assertEquals(FlappyBird.WIDTH / 2, bird.x, "La posición X inicial debe estar centrada");
        assertEquals(FlappyBird.HEIGHT / 2, bird.y, "La posición Y inicial debe estar centrada");
    }

    @Test
    public void testJumpSetsVerticalVelocity() {
        bird.jump();
        assertEquals(-8, bird.vy, 0.001, "El salto debe asignar velocidad vertical negativa");
    }

    @Test
    public void testPhysicsGravityAffectsVelocity() {
        float initialVy = bird.vy;
        bird.physics();
        assertTrue(bird.vy > initialVy, "La gravedad debe aumentar la velocidad vertical");
    }

    @Test
    public void testMakeInvincible() {
        bird.makeInvincible(10);
        assertTrue(bird.isInvincible(), "El pájaro debe estar invencible después de llamar makeInvincible()");
    }

    @Test
    public void testInvincibilityExpires() {
        bird.makeInvincible(2);
        bird.physics();
        bird.physics(); // debería expirar
        bird.physics(); // debería estar desactivado
        assertFalse(bird.isInvincible(), "La invencibilidad debe expirar después de cierto tiempo");
    }

    @Test
    public void testResetResetsPositionAndVelocity() {
        bird.jump();
        bird.physics();
        bird.reset();
        assertEquals(FlappyBird.WIDTH / 2, bird.x);
        assertEquals(FlappyBird.HEIGHT / 2, bird.y);
        assertEquals(0, bird.vx);
        assertEquals(0, bird.vy);
    }
}
