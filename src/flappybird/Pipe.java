package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pipe {
    private float x;
    private final int width;
    private final int height;
    private final boolean top;
    private Image headImg, bodyImg;

    public Pipe(int startX, int width, int height, boolean top, Image headImg, Image bodyImg) {
        this.x = startX;
        this.width = width;
        this.height = height;
        this.top = top;
        this.headImg = headImg;
        this.bodyImg = bodyImg;
    }

    public Pipe(int startX, int width, int height, boolean top) {
        this.x = startX;
        this.width = width;
        this.height = height;
        this.top = top;
        try {
            this.headImg = ImageIO.read(getClass().getResourceAsStream("/images/pipe.png"));
            this.bodyImg = ImageIO.read(getClass().getResourceAsStream("/images/pipe_part.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateState() {
        x -= 3;
    }

    public void draw(Graphics g) {
        int drawY = top ? 0 : GameModel.HEIGHT - height;
        g.drawImage(bodyImg, Math.round(x), drawY, width, height, null);

        if (top) {
            g.drawImage(headImg, Math.round(x - 5), height - 10, width + 10, 20, null);
        } else {
            g.drawImage(headImg, Math.round(x - 5), GameModel.HEIGHT - height, width + 10, 20, null);
        }
    }

    public Rectangle getBounds() {
        if (top) {
            return new Rectangle(Math.round(x), 0, width, height);
        } else {
            return new Rectangle(Math.round(x), GameModel.HEIGHT - height, width, height);
        }
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public float getX() {
        return x;
    }
}
