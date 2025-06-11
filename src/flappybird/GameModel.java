package flappybird;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modelo global del juego. Aquí vive toda la lógica de negocio:
 * - Estado de Bird, pipes, power-ups y defensores
 * - Generación de nuevos objetos
 * - Detección de colisiones y “game over”
 * - Puntuación y scroll
 */
public class GameModel {
    public static final int FPS = 80;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // Estado global
    private Bird bird;
    private List<Pipe> pipes;
    private List<PowerUp> powerUps;  // incluye GoldenBall y MiniMessi
    private List<Defender> defenders;

    private int score;
    private int scroll;
    private boolean paused;

    // Control de tiempos para spawn
    private long lastDefenderSpawnTime;
    private long lastMiniSpawnTime;

    public GameModel() {
        bird = new Bird();
        pipes = new ArrayList<>();
        powerUps = new ArrayList<>();
        defenders = new ArrayList<>();
        score = 0;
        scroll = 0;
        paused = true;
        lastDefenderSpawnTime = System.currentTimeMillis();
        lastMiniSpawnTime = System.currentTimeMillis();
    }

    public Bird getBird() {
        return bird;
    }

    public List<Pipe> getPipes() {
        return pipes;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<Defender> getDefenders() {
        return defenders;
    }

    public int getScore() {
        return score;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean p) {
        paused = p;
    }

    /** Hace que el pájaro salte (llamado desde el controlador) */
    public void birdJump() {
        bird.jump();
    }

    /** Reinicia todo el estado del juego */
    public void resetGame() {
        bird.reset();
        pipes.clear();
        powerUps.clear();
        defenders.clear();
        score = 0;
        scroll = 0;
        paused = true;
        lastDefenderSpawnTime = System.currentTimeMillis();
        lastMiniSpawnTime = System.currentTimeMillis();
    }

    /**
     * Se invoca en cada *tick* del bucle principal (cada 1000/FPS ms). Actualiza TODO el juego:
     * 1) Actualiza el estado del pájaro.
     * 2) Genera y mueve pipes; verifica colisión pájaro ↔ pipe.
     * 3) Genera y mueve GoldenBall; verifica colisión pájaro ↔ GoldenBall.
     * 4) Genera y mueve Defender; verifica colisión pájaro ↔ Defender.
     * 5) Genera y mueve MiniMessi; verifica colisión pájaro ↔ MiniMessi.
     * 6) Verifica si el pájaro se cayó fuera de los límites.
     * Si alguna colisión provoca “game over”, marca paused = true y retorna.
     */
    public void updateGameFrame() {
        bird.updateState();
        updateScoreAndScroll();              // Actualiza el puntaje y el scroll del juego
        generatePipes();                     // Genera nuevas tuberías cada cierto tiempo
        updatePipesAndCheckCollision();      // Mueve tuberías y detecta colisiones con el pájaro
        generateGoldenBall();                // Genera una GoldenBall si corresponde
        generateDefender();                  // Genera un nuevo defensor si han pasado 8 segundos
        updateDefendersAndCheckCollision();  // Mueve defensores y verifica colisiones
        generateMiniMessi();                 // Genera un MiniMessi si han pasado 10 segundos
        updatePowerUps();                    // Actualiza power-ups y aplica efectos si colisionan
        checkBirdOutOfBounds();              // Verifica si el pájaro se sale de la pantalla
    }

    private void updateScoreAndScroll() {
        score++;
        scroll++;
    }

    private void generatePipes() {
        if (scroll % 90 == 0) {
            int h1 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            int h2 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true));
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h2, false));
        }
    }

    private void updatePipesAndCheckCollision() {
        Iterator<Pipe> itPipe = pipes.iterator();
        while (itPipe.hasNext()) {
            Pipe p = itPipe.next();
            p.updateState();
            if (p.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                paused = true;
                return;
            }
            if (p.isOffScreen()) {
                itPipe.remove();
            }
        }
    }

    private void generateGoldenBall() {
        if (scroll % 700 != 0) return;

        int yPowerUp;
        boolean valid;
        int attempts = 0;

        do {
            yPowerUp = (int)(Math.random() * (HEIGHT - 100));
            valid = true;
            Rectangle ballBounds = new Rectangle(WIDTH, yPowerUp, 40, 40);
            for (Pipe pipe : pipes) {
                if (pipe.getBounds().intersects(ballBounds)) {
                    valid = false;
                    break;
                }
            }
            attempts++;
        } while (!valid && attempts < 10);

        powerUps.add(GoldenBall.getInstancia(WIDTH, yPowerUp));
    }

    private void generateDefender() {
        long now = System.currentTimeMillis();
        if (now - lastDefenderSpawnTime < 8000) return;

        int defenderX = WIDTH + 100;
        for (Pipe pipe : pipes) {
            if (Math.abs(pipe.getX() - defenderX) < 100) {
                return; // Evita superposición
            }
        }

        defenders.add(new Defender(defenderX));
        lastDefenderSpawnTime = now;
    }

    private void updateDefendersAndCheckCollision() {
        Iterator<Defender> itDef = defenders.iterator();
        while (itDef.hasNext()) {
            Defender d = itDef.next();
            d.updateState();
            if (d.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                paused = true;
                return;
            }
            if (d.isOffScreen()) {
                itDef.remove();
            }
        }
    }

    private void generateMiniMessi() {
        long now = System.currentTimeMillis();
        if (now - lastMiniSpawnTime < 10000) return;

        int spawnX = WIDTH + 50;
        int spawnY = (int)(Math.random() * (HEIGHT - 50));
        powerUps.add(MiniMessi.getInstancia(spawnX, spawnY));
        lastMiniSpawnTime = now;
    }

    private void updatePowerUps() {
        Iterator<PowerUp> itPU = powerUps.iterator();
        while (itPU.hasNext()) {
            PowerUp pu = itPU.next();
            pu.update();
            if (pu.getBounds().intersects(bird.getBounds())) {
                pu.applyEffect(bird);
                itPU.remove();
                continue;
            }
            if (pu.isOffScreen()) {
                itPU.remove();
            }
        }
    }

    private void checkBirdOutOfBounds() {
        if (bird.getY() > HEIGHT || bird.getY() + Bird.HEIGHT / 2f < 0) {
            paused = true;
        }
    }
}
