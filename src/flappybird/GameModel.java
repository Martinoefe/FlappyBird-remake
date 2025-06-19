package flappybird;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @class GameModel
 * @brief Almacena y actualiza todo el estado y reglas del juego.
 *
 * - Maneja Bird, Pipe, PowerUp y Defender.
 * - Genera nuevos objetos según cronogramas.
 * - Detecta colisiones y “game over”.
 * - Cuenta el puntaje y el scroll.
 */
public class GameModel {
    public static final int FPS    = 80;   ///< fotogramas por segundo
    public static final int WIDTH  = 800;  ///< ancho de ventana
    public static final int HEIGHT = 600;  ///< alto de ventana

    private Bird bird;                   ///< jugador
    private List<Pipe> pipes;            ///< tuberías
    private List<PowerUp> powerUps;      ///< power‑ups activos
    private List<Defender> defenders;    ///< defensores activos

    private int score;       ///< puntuación acumulada
    private int scroll;      ///< desplazamiento horizontal acumulado
    private boolean paused;  ///< true si el juego está en pausa

    private long lastDefenderSpawnTime; ///< instante del último defensor
    private long lastMiniSpawnTime;     ///< instante del último MiniMessi

    /**
     * @brief Constructor.
     * Inicializa listas, contador de puntaje, pausa y temporizadores de spawn.
     */
    public GameModel() {
        bird = new Bird();
        pipes = new ArrayList<>();
        powerUps = new ArrayList<>();
        defenders = new ArrayList<>();
        score = 0;
        scroll = 0;
        paused = true;
        lastDefenderSpawnTime = System.currentTimeMillis();
        lastMiniSpawnTime     = System.currentTimeMillis();
    }

    /** @return instancia de Bird (jugador). */
    public Bird getBird() { return bird; }

    /** @return lista de tuberías activas. */
    public List<Pipe> getPipes() { return pipes; }

    /** @return lista de power‑ups activos. */
    public List<PowerUp> getPowerUps() { return powerUps; }

    /** @return lista de defensores activos. */
    public List<Defender> getDefenders() { return defenders; }

    /** @return puntuación actual. */
    public int getScore() { return score; }

    /** @return true si el juego está en pausa. */
    public boolean isPaused() { return paused; }

    /**
     * @brief Cambia el estado de pausa.
     * @param p true para pausar, false para reanudar
     */
    public void setPaused(boolean p) { paused = p; }

    /** @brief Hace saltar al pájaro. */
    public void birdJump() { bird.jump(); }

    /** @brief Resetea todo el estado del juego. */
    public void resetGame() {
        bird.reset();
        pipes.clear();
        powerUps.clear();
        defenders.clear();
        score = 0;
        scroll = 0;
        paused = true;
        lastDefenderSpawnTime = System.currentTimeMillis();
        lastMiniSpawnTime     = System.currentTimeMillis();
    }

    /**
     * @brief Actualiza un fotograma completo de juego.
     * - Llama a todos los pasos de generación, movimiento y colisión.
     */
    public void updateGameFrame() {
        bird.updateState();
        updateScoreAndScroll();
        generatePipes();
        updatePipesAndCheckCollision();
        generateGoldenBall();
        generateDefender();
        updateDefendersAndCheckCollision();
        generateMiniMessi();
        updatePowerUps();
        checkBirdOutOfBounds();
    }

    /** Incrementa score y scroll. */
    private void updateScoreAndScroll() {
        score++;
        scroll++;
    }

    /** Genera tuberías cada 90 unidades de scroll. */
    private void generatePipes() {
        if (scroll % 90 == 0) {
            int h1 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            int h2 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true));
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h2, false));
        }
    }

    /** Mueve tuberías, detecta colisiones y remueve las que salen de pantalla. */
    private void updatePipesAndCheckCollision() {
        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe p = it.next();
            p.updateState();
            if (p.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                paused = true;
                return;
            }
            if (p.isOffScreen()) it.remove();
        }
    }

    /** Genera una GoldenBall según scroll y evita solapamiento. */
    private void generateGoldenBall() {
        if (scroll % 700 != 0) return;
        int yPU, attempts=0;
        boolean valid;
        do {
            yPU = (int)(Math.random() * (HEIGHT - 100));
            int finalYPU = yPU;
            valid = pipes.stream()
                    .noneMatch(pipe -> pipe.getBounds().intersects(
                            new Rectangle(WIDTH, finalYPU, 40,40)));
            attempts++;
        } while (!valid && attempts<10);
        powerUps.add(GoldenBall.getInstancia(WIDTH, yPU));
    }

    /** Genera un defensor cada 8s si no hay solapamiento. */
    private void generateDefender() {
        long now = System.currentTimeMillis();
        if (now - lastDefenderSpawnTime < 8000) return;
        int x0 = WIDTH + 100;
        boolean overlaps = pipes.stream()
                .anyMatch(pipe -> Math.abs(pipe.getX() - x0) < 100);
        if (!overlaps) {
            defenders.add(new Defender(x0));
            lastDefenderSpawnTime = now;
        }
    }

    /** Mueve defensores, detecta colisión y remueve los off-screen. */
    private void updateDefendersAndCheckCollision() {
        Iterator<Defender> it = defenders.iterator();
        while (it.hasNext()) {
            Defender d = it.next();
            d.updateState();
            if (d.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                paused = true;
                return;
            }
            if (d.isOffScreen()) it.remove();
        }
    }

    /** Genera un MiniMessi cada 10s. */
    private void generateMiniMessi() {
        long now = System.currentTimeMillis();
        if (now - lastMiniSpawnTime < 10000) return;
        powerUps.add(MiniMessi.getInstancia(WIDTH+50,
                (int)(Math.random()*(HEIGHT-50))));
        lastMiniSpawnTime = now;
    }

    /** Actualiza power‑ups, aplica efecto al colisionar y remueve off-screen. */
    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp pu = it.next();
            pu.update();
            if (pu.getBounds().intersects(bird.getBounds())) {
                pu.applyEffect(bird);
                it.remove();
                continue;
            }
            if (pu.isOffScreen()) it.remove();
        }
    }

    /** Finaliza juego si el pájaro sale de la pantalla. */
    private void checkBirdOutOfBounds() {
        if (bird.getY()>HEIGHT || bird.getY()+Bird.HEIGHT/2f<0) {
            paused = true;
        }
    }
}
