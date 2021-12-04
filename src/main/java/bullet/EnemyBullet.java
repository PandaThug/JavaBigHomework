package bullet;

import plane.Plane;

import java.awt.image.BufferedImage;

public class EnemyBullet extends Plane {
    private int dir;
    public EnemyBullet(BufferedImage aircraftImg, int ax, int ay, int dir) {
        this.aircraftImg = aircraftImg;
        w = this.aircraftImg.getWidth();
        h = this.aircraftImg.getHeight();
        x = ax;
        y = ay;
        this.dir = dir;
    }
    @Override
    public void move() {
        if (dir == 0) {
            x -= 1;
            y += 7;
        } else if (dir == 1) {
            y += 7;
        } else if (dir == 2) {
            x += 1;
            y += 7;
        }
    }
}
