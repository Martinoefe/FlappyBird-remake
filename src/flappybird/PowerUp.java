package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface PowerUp {
    void update();
    void draw(Graphics g);
    void applyEffect(Bird bird);
    boolean isActive();
    Rectangle getBounds();
}
