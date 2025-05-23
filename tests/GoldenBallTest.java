import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flappybird.GoldenBall;
import flappybird.Bird;

public class GoldenBallTest {

    private GoldenBall goldenBall;
    private Bird bird;

    /**
     * Inicializa una GoldenBall y un Bird antes de cada test.
     */
    @Before
    public void setUp() {
        goldenBall = new GoldenBall(400, 150);
        bird = new Bird();
    }

    /**
     * Verifica que el efecto se aplica correctamente y el pájaro queda invencible.
     */
    @Test
    public void testApplyEffectMakesBirdInvincible() {
        goldenBall.applyEffect(bird);
        assertTrue(bird.isInvincible());
    }

    /**
     * Verifica que GoldenBall se mueva hacia la izquierda.
     */
    @Test
    public void testGoldenBallMovesLeft() {
        int initialX = goldenBall.getX();
        goldenBall.update();
        assertTrue(goldenBall.getX() < initialX);
    }
}


