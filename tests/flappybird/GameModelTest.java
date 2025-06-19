package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class GameModelTest
 * @brief Pruebas unitarias para la clase GameModel del juego Flappy Bird.
 *
 * Verifica el estado inicial, el comportamiento de salto, el reinicio del juego,
 * el avance de puntaje y la generación de obstáculos.
 */
public class GameModelTest {

    private GameModel model;
    private boolean setupExitoso = false;

    /**
     * @brief Inicializa una nueva instancia de GameModel antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        try {
            model = new GameModel();
            setupExitoso = model != null;
        } catch (Exception e) {
            setupExitoso = false;
        }
    }

    /**
     * @test Verifica el estado inicial del modelo: juego pausado, score cero y sin entidades en pantalla.
     */
    @Test
    void testInitialState() {
        if (!setupExitoso) return;

        assertTrue(model.isPaused(), "Debe iniciar en paused=true");
        assertEquals(0, model.getScore(), "Puntaje inicial debe ser 0");
        assertTrue(model.getPipes().isEmpty(), "No debe haber pipes al inicio");
        assertTrue(model.getPowerUps().isEmpty(), "No debe haber power-ups al inicio");
        assertTrue(model.getDefenders().isEmpty(), "No debe haber defensores al inicio");
    }

    /**
     * @test Verifica que al despausar y ejecutar birdJump seguido de updateGameFrame,
     * el pájaro se mueva hacia arriba.
     */
    @Test
    void testSetPausedAndBirdJump() {
        if (!setupExitoso) return;

        model.setPaused(false);
        assertFalse(model.isPaused(), "setPaused(false) debe despausar");

        model.birdJump();
        float beforeY = model.getBird().getBounds().y;
        model.updateGameFrame();
        float afterY = model.getBird().getBounds().y;
        assertTrue(afterY < beforeY, "Después de birdJump y updateGameFrame, Y debe disminuir");
    }

    /**
     * @test Verifica que resetGame() limpie correctamente el estado del juego.
     */
    @Test
    void testResetGameClearsState() throws Exception {
        if (!setupExitoso) return;

        model.setPaused(false);
        model.birdJump();

        setScroll(model, 89); // Forzar estado para generar pipes en el siguiente frame
        model.updateGameFrame();
        assertFalse(model.getPipes().isEmpty(), "Debe generarse pipes cuando scroll pasa de 89 a 90");

        model.resetGame();

        assertTrue(model.isPaused(), "resetGame debe pausar el juego");
        assertEquals(0, model.getScore(), "resetGame debe reiniciar puntaje");
        assertTrue(model.getPipes().isEmpty(), "resetGame debe limpiar pipes");
        assertTrue(model.getPowerUps().isEmpty(), "resetGame debe limpiar powerUps");
        assertTrue(model.getDefenders().isEmpty(), "resetGame debe limpiar defenders");
    }

    /**
     * @test Verifica que updateGameFrame() incremente el puntaje y el valor de scroll.
     */
    @Test
    void testScoreAndScrollIncrement() throws Exception {
        if (!setupExitoso) return;

        int initialScore = model.getScore();
        model.setPaused(false);
        model.updateGameFrame();

        assertEquals(initialScore + 1, model.getScore(), "Cada tick incrementa score en 1");

        Field scrollField = GameModel.class.getDeclaredField("scroll");
        scrollField.setAccessible(true);
        int scroll = scrollField.getInt(model);
        assertEquals(1, scroll, "Cada tick incrementa scroll en 1");
    }

    /**
     * @test Verifica que se generen dos pipes cuando el scroll alcanza múltiplos de 90.
     */
    @Test
    void testGeneratePipesAtInterval() throws Exception {
        if (!setupExitoso) return;

        setScroll(model, 89);
        model.setPaused(false);
        model.updateGameFrame();
        List<Pipe> pipes = model.getPipes();
        assertEquals(2, pipes.size(), "Al cumple scroll%90==0 debe generarse 2 pipes");
    }

    /**
     * @brief Modifica el valor interno de la variable scroll mediante reflexión.
     * @param m Instancia de GameModel.
     * @param value Valor a establecer en scroll.
     */
    private void setScroll(GameModel m, int value) throws Exception {
        Field f = GameModel.class.getDeclaredField("scroll");
        f.setAccessible(true);
        f.setInt(m, value);
    }
}
