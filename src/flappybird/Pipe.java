package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pipe {
    private float x;
    private final int width;
    private final int height;
    private final boolean top;  // true = tubería superior, false = tubería inferior
    private Image headImg, bodyImg;

    public Pipe(int startX, int width, int height, boolean top, Image headImg, Image bodyImg) {
        this.x = startX;
        this.width = width;
        this.height = height;
        this.top = top;
        this.headImg = headImg;
        this.bodyImg = bodyImg;
    }

    /**
     * Constructor alternativo si quieres cargar imágenes dentro:
     * (pero normalmente el controlador inyecta las imágenes)
     */
    public Pipe(int startX, int width, int height, boolean top) {
        this.x = startX;
        this.width = width;
        this.height = height;
        this.top = top;
        try {
            this.headImg = ImageIO.read(new File("images/pipe.png"));
            this.bodyImg = ImageIO.read(new File("images/pipe_part.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Se mueve 3 px hacia la izquierda cada frame */
    public void updateState() {
        x -= 3;
    }

    /** Dibuja cuerpo y cabeza de la tubería */
    public void draw(Graphics g) {
        // Dibujo del cuerpo
        int drawY = top ? 0 : GameModel.HEIGHT - height;
        g.drawImage(bodyImg, Math.round(x), drawY, width, height, null);

        // Dibujo de la cabeza decorativa (20 px de alto aprox.)
        if (top) {
            g.drawImage(headImg, Math.round(x - 5), height - 10, width + 10, 20, null);
        } else {
            g.drawImage(headImg, Math.round(x - 5), GameModel.HEIGHT - height, width + 10, 20, null);
        }
    }

    /** Hitbox de la tubería (superior o inferior) */
    public Rectangle getBounds() {
        if (top) {
            return new Rectangle(Math.round(x), 0, width, height);
        } else {
            return new Rectangle(Math.round(x), GameModel.HEIGHT - height, width, height);
        }
    }

    /** @return true si ya salió totalmente a la izquierda */
    public boolean isOffScreen() {
        return x + width < 0;
    }

    /** Para revisar X desde fuera (opcional) */
    public float getX() {
        return x;
    }
}
