package flappybird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    private GameModel model;

    @BeforeEach
    void setUp() {
        model = new GameModel();
    }

    @Test
    void testInitialState() {
        assertTrue(model.isPaused(), "Debe iniciar en paused=true");
        assertEquals(0, model.getScore(), "Puntaje inicial debe ser 0");
        assertTrue(model.getPipes().isEmpty(), "No debe haber pipes al inicio");
        assertTrue(model.getPowerUps().isEmpty(), "No debe haber power-ups al inicio");
        assertTrue(model.getDefenders().isEmpty(), "No debe haber defensores al inicio");
    }

    @Test
    void testSetPausedAndBirdJump() {
        model.setPaused(false);
        assertFalse(model.isPaused(), "setPaused(false) debe despausar");
        model.birdJump();
        // Después de birdJump + updateGameFrame, la y del bird debe disminuir
        float beforeY = model.getBird().getBounds().y;
        model.updateGameFrame();
        float afterY = model.getBird().getBounds().y;
        assertTrue(afterY < beforeY, "Después de birdJump y updateGameFrame, Y debe disminuir");
    }

    @Test
    void testResetGameClearsState() throws Exception {
        // Despausar para que updateGameFrame() haga todo el ciclo
        model.setPaused(false);
        model.birdJump(); // solo para cambiar algo en el estado

        // Forzar scroll=89, tras updateGameFrame() será 90 → genera pipes
        setScroll(model, 89);
        model.updateGameFrame();
        assertFalse(model.getPipes().isEmpty(),
                "Debe generarse pipes cuando scroll pasa de 89 a 90");

        // Ahora resetear y comprobar limpieza total
        model.resetGame();
        assertTrue(model.isPaused(),    "resetGame debe pausar el juego");
        assertEquals(0, model.getScore(),      "resetGame debe reiniciar puntaje");
        assertTrue(model.getPipes().isEmpty(),"resetGame debe limpiar pipes");
        assertTrue(model.getPowerUps().isEmpty(),"resetGame debe limpiar powerUps");
        assertTrue(model.getDefenders().isEmpty(),"resetGame debe limpiar defenders");
    }


    @Test
    void testScoreAndScrollIncrement() throws Exception {
        int initialScore = model.getScore();
        // Llamar varias veces updateGameFrame sin colisiones
        model.setPaused(false);
        model.updateGameFrame();
        assertEquals(initialScore + 1, model.getScore(), "Cada tick incrementa score en 1");
        // Verificar que scroll también sube
        Field scrollField = GameModel.class.getDeclaredField("scroll");
        scrollField.setAccessible(true);
        int scroll = scrollField.getInt(model);
        assertEquals(1, scroll, "Cada tick incrementa scroll en 1");
    }

    @Test
    void testGeneratePipesAtInterval() throws Exception {
        // Forzar scroll a 89 para que al next tick sea 90
        setScroll(model, 89);
        model.setPaused(false);
        model.updateGameFrame();
        List<Pipe> pipes = model.getPipes();
        assertEquals(2, pipes.size(), "Al cumple scroll%90==0 debe generarse 2 pipes");
    }

    // Helpers para manipular scroll con reflexión
    private void setScroll(GameModel m, int value) throws Exception {
        Field f = GameModel.class.getDeclaredField("scroll");
        f.setAccessible(true);
        f.setInt(m, value);
    }
}
