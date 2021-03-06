package plane;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 敌人的飞机
 */
public class FirstBadAircraft extends Plane {
    // 飞机移动的速度
    private int step;
    // 这个飞机的分数
    private int score;

    public FirstBadAircraft(BufferedImage aircraftImg, int score) {
        Random random = new Random();
        // 定义飞机的图片
        this.aircraftImg = aircraftImg;

        // 定义飞机的宽和高
        w = aircraftImg.getWidth();
        h = aircraftImg.getHeight();

        // 定义飞机的初始位置
        this.x = random.nextInt(512 - w);
        this.y = -h;

        // 设置敌对飞机移动的速度
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
