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
     * Se invoca cada tick (1000/FPS ms). Actualiza TODO el juego:
     * 1) bird.updateState()
     * 2) Generar y mover pipes; colisión bird↔pipe
     * 3) Generar y mover GoldenBall; colisión bird↔GoldenBall
     * 4) Generar y mover Defender; colisión bird↔Defender
     * 5) Generar y mover MiniMessi; colisión bird↔MiniMessi
     * 6) Verificar caída del pájaro
     * Si alguna colisión produce “game over”, marca paused=true y retorna.
     */
    public void updateGameFrame() {
        // 1) Actualizar pájaro
        bird.updateState();

        // 2) Incrementar puntaje/scroll
        score++;
        scroll++;

        // 3) Generar tuberías cada 90 frames
        if (scroll % 90 == 0) {
            int h1 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            int h2 = (int)((Math.random() * HEIGHT) / 5f + 0.2f * HEIGHT);
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h1, true));
            pipes.add(new Pipe(WIDTH, GamePanel.PIPE_W, h2, false));
        }

        // 4) Actualizar tuberías y colisión bird↔pipe
        Iterator<Pipe> itPipe = pipes.iterator();
        while (itPipe.hasNext()) {
            Pipe p = itPipe.next();
            p.updateState();
            Rectangle birdBounds = bird.getBounds();
            if (p.getBounds().intersects(birdBounds) && !bird.isInvincible()) {
                // Game Over
                paused = true;
                return;
            }
            if (p.isOffScreen()) {
                itPipe.remove();
            }
        }

        // 5) Generar GoldenBall cada 700 frames
        if (scroll % 700 == 0) {
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

        // 6) Actualizar GoldenBall y colisión bird↔GoldenBall
        Iterator<PowerUp> itPU = powerUps.iterator();
        while (itPU.hasNext()) {
            PowerUp pu = itPU.next();
            pu.update();
            if (pu instanceof GoldenBall) {
                GoldenBall gb = (GoldenBall) pu;
                if (gb.getBounds().intersects(bird.getBounds())) {
                    gb.applyEffect(bird);
                    itPU.remove();
                    continue;
                }
                if (gb.isOffScreen()) {
                    itPU.remove();
                }
            }
        }

        // 7) Generar Defender cada 8000 ms
        long now = System.currentTimeMillis();
        if (now - lastDefenderSpawnTime >= 8000) {
            int defenderX = WIDTH + 100;
            boolean overlaps = false;
            for (Pipe pipe : pipes) {
                if (Math.abs(pipe.getX() - defenderX) < 100) {
                    overlaps = true;
                    break;
                }
            }
            if (!overlaps) {
                defenders.add(new Defender(defenderX));
                lastDefenderSpawnTime = now;
            }
        }

        // 8) Actualizar Defender y colisión bird↔Defender
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

        // 9) Generar MiniMessi cada 10000 ms
        if (now - lastMiniSpawnTime >= 10000) {
            int spawnX = WIDTH + 50;
            int spawnY = (int)(Math.random() * (HEIGHT - 50));
            powerUps.add(MiniMessi.getInstancia(spawnX, spawnY));
            lastMiniSpawnTime = now;
        }

        // 10) Actualizar MiniMessi y colisión bird↔MiniMessi
        itPU = powerUps.iterator();
        while (itPU.hasNext()) {
            PowerUp pu = itPU.next();
            if (pu instanceof MiniMessi) {
                MiniMessi mm = (MiniMessi) pu;
                mm.update();
                if (mm.getBounds().intersects(bird.getBounds()) && !bird.isInvincible()) {
                    mm.applyEffect(bird);
                    itPU.remove();
                    continue;
                }
                if (mm.isOffScreen()) {
                    itPU.remove();
                }
            }
        }

        // 11) Verificar caída del pájaro (fuera de pantalla vertical)
        if (bird.getY() > HEIGHT || bird.getY() + Bird.HEIGHT/2f < 0) {
            paused = true;
        }
    }
}
