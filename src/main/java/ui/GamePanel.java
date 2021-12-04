package ui;

import bullet.BossBullet;
import bullet.EnemyBullet;
import bullet.PlayerBullet;
import plane.Aircraft;
import plane.BossBadAircraft;
import plane.FirstBadAircraft;
import plane.SecondBadAircraft;
import tool.ImageTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 游戏的面板
 * 1.继承JPanel
 * 2.
 */
public class GamePanel extends JPanel {
    // 背景图片
    private final BufferedImage bgi;
    // 用户飞机
    private final Aircraft aircraft;
    // 敌机集合
    private final List<FirstBadAircraft> firstBadAircraftList = new ArrayList<>();
    private final List<SecondBadAircraft> secondBadAircraftList = new ArrayList<>();
    private final List<BossBadAircraft> bossBadAircraftList = new ArrayList<>();
    // 子弹集合
    private final List<PlayerBullet> playerBulletList = new ArrayList<>();
    private final List<EnemyBullet> enemyBulletList = new ArrayList<>();
    private final List<BossBullet> bossBulletList = new ArrayList<>();
    int badAircraftSum = 0;
    int playerBulletNum = 0;
    int enemyBulletNum = 0;
    int bossBulletNum = 0;
    int secondBadAircraft = 0;
    int firstBadAircraft = 0;
    // 分数
    private int score;
    private Thread mThread = null;
    private boolean isRunning = false;

    /**
     * 构造函数
     */
    public GamePanel(GameFrame frame) {
        // 1.读取背景图片
        bgi = ImageTool.getImg("/img/background.png");

        // 2.初始化飞机
        // - 用户操作的飞机
        aircraft = new Aircraft(ImageTool.getImg("/img/me1.png"));

        // 3.使用鼠标来控制飞机
        // - 创建鼠标适配器
        MouseAdapter ma = new MouseAdapter() {
            // - 确定鼠标需要监听的事件
            //      a.鼠标移动  b.鼠标点击 c.鼠标按下去  d.鼠标移入窗口 e.鼠标移出窗口
            @Override
            public void mouseMoved(MouseEvent e) {
                // - 鼠标控制飞机移动
                aircraft.move(e);
                // - 重新绘制飞机的位置
                repaint();
            }
        };
        // - 注册鼠标适配器
        addMouseListener(ma);
        addMouseMotionListener(ma);

        // 4.使用键盘适配器
        KeyAdapter ka = new KeyAdapter() {
            // - 确定键盘需要监听的事件
            //      a.按下键盘按键
            @Override
            public void keyPressed(KeyEvent e) {
                // - 键盘控制飞机移动
                aircraft.move(e, 25);
                // - 重新渲染飞机的位置
                repaint();
            }
        };
        // - 注册键盘适配器到游戏窗口中，直接注册到界面中无效
        frame.addKeyListener(ka);
    }

    /**
     * 专用画图方法
     *
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g) {
        // 1.添加图片 - 背景
        g.drawImage(bgi, 0, 0, null);

        // 2.添加飞机

        // - 用户操作的飞机
        g.drawImage(aircraft.getAircraftImg(), aircraft.getX(), aircraft.getY(), aircraft.getW(), aircraft.getH(),
                null);
        // - 敌机（小兵）
        for (int i = 0; i < firstBadAircraftList.size(); i++) {
            g.drawImage(firstBadAircraftList.get(i).getAircraftImg(),
                    firstBadAircraftList.get(i).getX(), firstBadAircraftList.get(i).getY(),
                    firstBadAircraftList.get(i).getW(), firstBadAircraftList.get(i).getH(), null);
        }
        for (int i = 0; i < secondBadAircraftList.size();i++) {
            g.drawImage(secondBadAircraftList.get(i).getAircraftImg(),
                    secondBadAircraftList.get(i).getX(), secondBadAircraftList.get(i).getY(),
                    secondBadAircraftList.get(i).getW(), secondBadAircraftList.get(i).getH(),null);
        }
        for (int i = 0; i < bossBadAircraftList.size();i++) {
            g.drawImage(bossBadAircraftList.get(i).getAircraftImg(),
                    bossBadAircraftList.get(i).getX(), bossBadAircraftList.get(i).getY(),
                    bossBadAircraftList.get(i).getW(), bossBadAircraftList.get(i).getH(), null);
        }
        // - 添加子弹
        for (int i = 0; i < playerBulletList.size(); i++) {
            g.drawImage(playerBulletList.get(i).getAircraftImg(),
                    playerBulletList.get(i).getX(), playerBulletList.get(i).getY(),
                    playerBulletList.get(i).getW(), playerBulletList.get(i).getH(), null);
        }
        for (int i = 0; i < enemyBulletList.size();i++) {
            g.drawImage(enemyBulletList.get(i).getAircraftImg(),
                    enemyBulletList.get(i).getX(), enemyBulletList.get(i).getY(),
                    enemyBulletList.get(i).getW(), enemyBulletList.get(i).getH(), null);
        }
        for (int i = 0; i < bossBulletList.size();i++) {
            g.drawImage(bossBulletList.get(i).getAircraftImg(),
                    bossBulletList.get(i).getX(), bossBulletList.get(i).getY(),
                    bossBulletList.get(i).getW(), bossBulletList.get(i).getH(), null);
        }

        // 3.添加分数
        g.setColor(Color.black);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString("分数：" + score, 10, 30);
    }

    /**
     * 游戏开始
     */
    public void action() {
        mThread = new Thread();
        isRunning = true;
        // 启动一个线程
        while (isRunning) {
            // 生成敌机
            generateBadAircraft();
            // 敌机移动
            moveFirstBadAircraft();
            moveSecondBadAircraft();
            // 生成子弹
            generatePlayerBullet();
            generateEnemyBullet();
            generateBossBullet();
            // 子弹移动
            movePlayerBullet();
            moveEnemyBullet();
            moveBossBullet();
            // 用户的飞机的子弹是否击中的敌机
            shootBad();
            //用户的飞机是否被敌机击毁
            shootPlayer();
            //用户飞机是否与敌机发生碰撞
            Collision();
            // 重新渲染游戏界面
            repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否击中,增加分数。
     */
    private void shootBad() {
        // 遍历所有的子弹 (不能用增强for，有ConcurrentModificationException，因为增强for是使用了迭代器)
        for (int i = 0; i < playerBulletList.size(); i++) {
            PlayerBullet playerBullet = playerBulletList.get(i);
            // 遍历所有的敌机
            for (int j = 0; j < firstBadAircraftList.size(); j++) {
                FirstBadAircraft firstBadAircraft = firstBadAircraftList.get(j);
                // 如果击中，删除并增加粉丝
                if (firstBadAircraft.shootBy(playerBullet)) {
                    firstBadAircraft.HP -= 1;
                    if (firstBadAircraft.HP <= 0) {
                        // - 敌机删除
                        firstBadAircraftList.remove(firstBadAircraft);
                        // - 增加分数
                        this.score += firstBadAircraft.getScore();
                        // - 子弹删除
                        playerBulletList.remove(playerBullet);
                    }
                }
            }
            for (int j = 0; j < secondBadAircraftList.size(); j++) {
                SecondBadAircraft secondBadAircraft = secondBadAircraftList.get(j);
                // 如果击中，删除并增加分数
                if (secondBadAircraft.shootBy(playerBullet)) {
                    secondBadAircraft.HP -= 1;
                    if (secondBadAircraft.HP <= 0) {
                        // - 敌机删除
                        secondBadAircraftList.remove(secondBadAircraft);
                        // - 增加分数
                        this.score += secondBadAircraft.getScore();
                        // - 子弹删除
                        playerBulletList.remove(playerBullet);
                    }
                }
            }
            for (int j = 0; j < bossBadAircraftList.size(); j++) {
                BossBadAircraft bossBadAircraft = bossBadAircraftList.get(j);
                // 如果击中，删除并增加粉丝
                if (bossBadAircraft.shootBy(playerBullet)) {
                    bossBadAircraft.HP -= 1;
                    if (bossBadAircraft.HP <= 0) {
                        // - 敌机删除
                        bossBadAircraftList.remove(bossBadAircraft);
                        // - 增加分数
                        this.score += bossBadAircraft.getScore();
                        // - 子弹删除
                        playerBulletList.remove(playerBullet);
                    }
                }
            }
        }
    }
    private void shootPlayer() {
        for (int i = 0; i < enemyBulletList.size(); i++) {
            EnemyBullet enemyBullet = enemyBulletList.get(i);
            if (aircraft.shootBy(enemyBullet)) {
                aircraft.HP -= 1;
                if (aircraft.HP <= 0) {
                    System.out.println("！！！！！！！！！我被击毁了！！！！！！！！！");
                    isRunning = false;
                }
            }
        }
        for (int i = 0; i < bossBulletList.size(); i++) {
            BossBullet bossBullet = bossBulletList.get(i);
            if (aircraft.shootBy(bossBullet)) {
                aircraft.HP -= 2;
                if (aircraft.HP <= 0) {
                    System.out.println("！！！！！！！！！我被击毁了！！！！！！！！！");
                    isRunning = false;
                }
            }
        }
    }
    private void Collision() {
        for (int i = 0; i < firstBadAircraftList.size(); i++) {
            if (aircraft.getX() >= firstBadAircraftList.get(i).getX() &&
                    aircraft.getX() < firstBadAircraftList.get(i).getX() + firstBadAircraftList.get(i).getW() &&
                    aircraft.getY() >= firstBadAircraftList.get(i).getY() &&
                    aircraft.getY() < firstBadAircraftList.get(i).getY() + firstBadAircraftList.get(i).getH()) {
                System.out.println("！！！！！！！！！我被撞毁了！！！！！！！！！");
                isRunning = false;
            }
        }
        for (int i = 0; i < secondBadAircraftList.size(); i++) {
            if (aircraft.getX() >= secondBadAircraftList.get(i).getX() &&
                    aircraft.getX() < secondBadAircraftList.get(i).getX() + secondBadAircraftList.get(i).getW() &&
                    aircraft.getY() >= secondBadAircraftList.get(i).getY() &&
                    aircraft.getY() < secondBadAircraftList.get(i).getY() + secondBadAircraftList.get(i).getH()) {
                System.out.println("！！！！！！！！！我被撞毁了！！！！！！！！！");
                isRunning = false;
            }
        }
        for (int i = 0; i < bossBadAircraftList.size(); i++) {
            if (aircraft.getX() >= bossBadAircraftList.get(i).getX() &&
                    aircraft.getX() < bossBadAircraftList.get(i).getX() + bossBadAircraftList.get(i).getW() &&
                    aircraft.getY() >= bossBadAircraftList.get(i).getY() &&
                    aircraft.getY() < bossBadAircraftList.get(i).getY() + bossBadAircraftList.get(i).getH()) {
                System.out.println("！！！！！！！！！我被撞毁了！！！！！！！！！");
                isRunning = false;
            }
        }
    }

    /**
     * 移动子弹
     */
    private void movePlayerBullet() {
        for (PlayerBullet playerBullet : playerBulletList) {
            playerBullet.move();
        }
    }
    private void moveEnemyBullet() {
        for (EnemyBullet enemyBullet : enemyBulletList) {
            enemyBullet.move();
        }
    }
    private void moveBossBullet() {
        for (BossBullet bossBullet : bossBulletList) {
            bossBullet.move();
        }
    }

    /**
     * 生成子弹
     */
    private void generatePlayerBullet() {
        playerBulletNum++;
        if (playerBulletNum == 30) {
            // 创建子弹对象
            PlayerBullet playerBullet1 = new PlayerBullet(ImageTool.getImg("/img/bullet1.png"),
                    aircraft.getX() - 5, aircraft.getY(), 0);
            PlayerBullet playerBullet2 = new PlayerBullet(ImageTool.getImg("/img/bullet1.png"),
                    aircraft.getX() + 20, aircraft.getY() - 20, 1);
            PlayerBullet playerBullet3 = new PlayerBullet(ImageTool.getImg("/img/bullet1.png"),
                    aircraft.getX() + 50, aircraft.getY(), 2);
            // 添加子弹
            playerBulletList.add(playerBullet1);
            playerBulletList.add(playerBullet2);
            playerBulletList.add(playerBullet3);
            playerBulletNum = 0;
        }
    }
    private void generateEnemyBullet() {
        for (FirstBadAircraft fba : firstBadAircraftList) {
            enemyBulletNum++;
            if (enemyBulletNum == 80) {
//                EnemyBullet enemyBullet1 = new EnemyBullet(ImageTool.getImg("/img/bullet2.png"),
//                        fba.getX() - 5, fba.getY(), 0);
                EnemyBullet enemyBullet2 = new EnemyBullet(ImageTool.getImg("/img/bullet2.png"),
                        fba.getX() + 20, fba.getY() + 20, 1);
//                EnemyBullet enemyBullet3 = new EnemyBullet(ImageTool.getImg("/img/bullet2.png"),
//                        fba.getX() + 50, fba.getY(), 2);
//                enemyBulletList.add(enemyBullet1);
                enemyBulletList.add(enemyBullet2);
//                enemyBulletList.add(enemyBullet3);
                enemyBulletNum = 0;
            }
        }
    }
    private void generateBossBullet() {
        for (BossBadAircraft bba : bossBadAircraftList) {
            bossBulletNum++;
            if (bossBulletNum == 80) {
                BossBullet bossBullet1 = new BossBullet(ImageTool.getImg("/img/bullet3.png"),
                        bba.getX() - 5, bba.getY(), 0);
                BossBullet bossBullet2 = new BossBullet(ImageTool.getImg("/img/bullet3.png"),
                        bba.getX() + 20, bba.getY() + 20, 0);
                BossBullet bossBullet3 = new BossBullet(ImageTool.getImg("/img/bullet3.png"),
                        bba.getX() + 50, bba.getY(), 0);
                bossBulletList.add(bossBullet1);
                bossBulletList.add(bossBullet2);
                bossBulletList.add(bossBullet3);
                bossBulletNum = 0;
            }
        }
    }

    /**
     * 敌机移动
     */
    private void moveFirstBadAircraft() {
        for (FirstBadAircraft fba : firstBadAircraftList) {
            // 移动
            fba.move();
        }
    }
    private void moveSecondBadAircraft() {
        for (SecondBadAircraft sba : secondBadAircraftList) {
            sba.move();
        }
    }

    /**
     * 生成敌机
     * - 根据当前方法运行了多少次，来决定生成哪种敌机
     */
    private void generateBadAircraft() {
        // 生成敌机并保存
        badAircraftSum++;
        // 当一个敌机移动二十次就生成另一个新的敌机(小兵)
        if (badAircraftSum >= 20) {
            FirstBadAircraft firstBadAircraft = new FirstBadAircraft(ImageTool.getImg("/img/enemy1.png"), 1);
            firstBadAircraftList.add(firstBadAircraft);

            badAircraftSum = 0;
            secondBadAircraft++;
        }
        // 生成第二大的战机
        if (secondBadAircraft >= 5) {
            FirstBadAircraft firstBadAircraft = new FirstBadAircraft(ImageTool.getImg("/img/enemy2.png"), 10);
            firstBadAircraftList.add(firstBadAircraft);

            secondBadAircraft = 0;
            this.firstBadAircraft++;
        }
        // 生成最大的敌机
        if (firstBadAircraft >= 5) {
            BossBadAircraft bossBadAircraft = new BossBadAircraft(ImageTool.getImg("/img/enemy3.png"),100);
            // 设置移动速度
            bossBadAircraft.setStep(1);
            bossBadAircraftList.add(bossBadAircraft);

            this.firstBadAircraft = 0;
        }
    }
}
