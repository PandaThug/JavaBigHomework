package bullet;

import plane.Plane;

import java.awt.image.BufferedImage;

/**
 * @author CDConlyred
 */
public class BossBullet extends Plane {
    private int dir;
    public BossBullet(BufferedImage aircraftImg, int ax, int ay, int dir) {
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
            //左下角
            x -= 1;
            y += 7;
        } else if (dir == 1) {
            //垂直
            y += 7;
        } else if (dir == 2) {
            //右下角
            x += 1;
            y += 7;
        }
    }
}
