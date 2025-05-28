import flappybird.GoldenBall;
import flappybird.Bird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class GoldenBallTest {

    private GoldenBall goldenBall;

    @BeforeEach
    void setUp() {
        goldenBall = new GoldenBall(200, 100); // posición inicial
    }

    @Test
    void testInitialPosition() {
        assertEquals(200, goldenBall.getX());
        Rectangle bounds = goldenBall.getBounds();
        assertEquals(new Rectangle(200, 100, 30, 30), bounds);
    }

    @Test
    void testUpdateMovesLeft() {
        int xBefore = goldenBall.getX();
        goldenBall.update();
        assertEquals(xBefore - 3, goldenBall.getX());
    }

    @Test
    void testIsOffScreenFalseWhenVisible() {
        assertFalse(goldenBall.isOffScreen());
    }
/*
    @Test
    void testIsOffScreenTrueWhenOut() {
        // Moverlo fuera de la pantalla
        for (int i = 0; i < 70; i++) {
            goldenBall.update(); // 70 * 3 = 210 px a la izquierda
        }
        assertTrue(goldenBall.isOffScreen());
    }
*/
    @Test
    void testApplyEffectActivatesPowerUp() {
        Bird dummyBird = new DummyBird();
        goldenBall.applyEffect(dummyBird);
        assertTrue(goldenBall.isActive());
    }

    @Test
    void testPowerUpExpiresAfterTime() throws InterruptedException {
        Bird dummyBird = new DummyBird();
        goldenBall.applyEffect(dummyBird);
        assertTrue(goldenBall.isActive(), "GoldenBall debería estar activo inicialmente");

        // Esperar más de 5 segundos para asegurar que el power-up expire
        Thread.sleep(5500);
        goldenBall.update(); // Debe verificar y actualizar el estado interno
        assertFalse(goldenBall.isActive(), "GoldenBall debería estar inactivo después de 5.5 segundos");
    }

    /**
     * Clase dummy para simular un pájaro en los tests.
     */
    static class DummyBird extends Bird {
        private boolean invincibleFlag = false;

        @Override
        public void makeInvincible(int frames) {
            invincibleFlag = true;
        }

        @Override
        public boolean isInvincible() {
            return invincibleFlag;
        }
    }
}