import static org.junit.Assert.*;
import org.junit.Test;
import flappybird.Bird;
import flappybird.GoldenBall;

/**
 * Test para verificar que al aplicar el efecto del GoldenBall,
 * Messi se vuelve invencible.
 */
public class GoldenBallTest {

    @Test
    public void testGoldenBallAppliesInvincibility() {
        Bird bird = new Bird();
        GoldenBall gb = new GoldenBall(100, 100); // posición arbitraria
        gb.applyEffect(bird);
        assertTrue(bird.isInvincible());
    }
}

