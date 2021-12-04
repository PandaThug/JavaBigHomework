package plane;

import java.awt.image.BufferedImage;
import java.util.Random;

public class SecondBadAircraft extends Plane {
    private int step;
    private int score;
    public SecondBadAircraft(BufferedImage aircraftImg, int score) {
        Random random = new Random();
        this.aircraftImg = aircraftImg;
        w = aircraftImg.getWidth();
        h = aircraftImg.getHeight();
        this.x = random.nextInt(512 - w);
        this.y = -h;
        step = random.nextInt(6);
        this.score = score;
        this.HP = 2;
    }
    @Override
    public void move() {
        y += step;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
