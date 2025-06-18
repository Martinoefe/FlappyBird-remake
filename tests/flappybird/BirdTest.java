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
        assertEquals(GameModel.WIDTH / 2f, bird.getBounds().getCenterX(), 0.01);
        assertEquals(GameModel.HEIGHT / 2f, bird.getY(), 0.01);
    }

    @Test
    public void testJumpChangesVerticalPosition() {
        float initialY = bird.getY();
        bird.jump();
        bird.updateState();
        assertTrue(bird.getY() < initialY); // Porque vy es negativo después del salto
    }

    @Test
    public void testInvincibilityActivationAndExpiration() {
        bird.makeInvincible(2);
        assertTrue(bird.isInvincible());
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isInvincible());
    }

    @Test
    public void testMiniActivationAndExpiration() {
        bird.makeMini(2);
        assertTrue(bird.isMini());
        bird.updateState(); // frame 1
        bird.updateState(); // frame 2
        assertFalse(bird.isMini());
    }

    @Test
    public void testBoundsAreSmallerWhenMini() {
        Rectangle normal = bird.getBounds();
        bird.makeMini(10);
        bird.updateState();
        Rectangle mini = bird.getBounds();

        assertTrue(mini.width < normal.width);
        assertTrue(mini.height < normal.height);
    }

    @Test
    public void testResetRestoresInitialState() {
        bird.jump();
        bird.makeInvincible(10);
        bird.makeMini(10);
        bird.updateState();

        bird.reset();
        assertEquals(GameModel.WIDTH / 2f, bird.getBounds().getCenterX(), 0.01);
        assertEquals(GameModel.HEIGHT / 2f, bird.getY(), 0.01);
        assertFalse(bird.isInvincible());
        assertFalse(bird.isMini());
    }
}
