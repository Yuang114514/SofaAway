package cn.yuang2714.sofaaway;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sofa extends JFrame implements Runnable {
    private final int ASSOCIATED_SOFA_COUNT = 10;
    private final Clip music = AudioSystem.getClip();
    private final BufferedImage drawingPane;
    private final Graphics2D graphics;
    private List<AssociatedSofa> associations;
    private AssociatedSofa mainSofa;
    private final ScreenInfo screenInfo;
    private final Random random = new Random();
    private volatile boolean isFinished = false;
    private final JPanel panel;
    
    private record ScreenInfo(Rectangle largest ,int mediumY, int wayLength) {}
    
    public Sofa() throws Exception {
        //基本设置
        setTitle("Sofa Away");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);

        //透明背景
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        
        //设置图标
        ImageIcon sofaImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofas/white_sofa.png")));
        setIconImage(sofaImage.getImage());
        
        //获取屏幕信息
        screenInfo = getScreenInfo();
        System.out.println("窗口：" + screenInfo.largest);
        
        //设置窗口
        setLocation(screenInfo.largest.x, screenInfo.largest.y);
        setSize(screenInfo.largest.getSize());
        
        //设置JPanel容器
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(drawingPane, 0,0,null);
            }
        };
        panel.setOpaque(false);
        add(panel);
        
        //设置画布
        drawingPane = new BufferedImage(screenInfo.largest.width, screenInfo.largest.height, BufferedImage.TYPE_INT_ARGB);
        graphics = drawingPane.createGraphics();
        
        //清空背景
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0,0,screenInfo.largest.width,screenInfo.largest.height);
        
        //调整画笔模式
        graphics.setComposite(AlphaComposite.SrcOver);
        
        setVisible(true);
    }
    
    @SuppressWarnings("BusyWait")
    public void away() throws Exception {
        //将主沙发设置好
        System.out.println("正在设置主沙发......");
        mainSofa = new AssociatedSofa(
                screenInfo.mediumY - 150,
                0,
                screenInfo.wayLength,
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofas/white_sofa.png"))).getImage(),
                14500
        );
        System.out.println("主沙发设置完毕。路程：" + screenInfo.wayLength + "像素");
        
        //设置伴生沙发（初始5个）
        System.out.println("正在设置伴生沙发......");
        associations = new CopyOnWriteArrayList<>();
        for (int i = 0; i < ASSOCIATED_SOFA_COUNT; i++) {
            associations.add(randomAssociatedSofa());
        }
        
        //记录开始时间
        long startTime = System.currentTimeMillis();
        
        //设置更新线程
        Thread updateThread = new Thread(this, "Sofa Update Thread");
        
        //启动音乐，并等待音乐唱 “So far away”
        startMusic();
        mainSofa.start();
        associations.forEach(AssociatedSofa::start);
        mainSofa.update(graphics);
        Thread.sleep(1500);
        updateThread.start();
        
        //监控移动
        while (true) {
            Thread.sleep(10);
            if (System.currentTimeMillis() >= startTime + 14000) {
                isFinished = true;
                Thread.sleep(500);
                System.out.println("沙发移动完毕");
                return;
            }
        }
    }
    
    private AssociatedSofa randomAssociatedSofa() {
        int startX = random.nextInt(0, screenInfo.wayLength - 400);
        int wayLength = random.nextInt(screenInfo.wayLength / 8, screenInfo.wayLength / 2);
        int y = random.nextInt(screenInfo.largest.y, screenInfo.largest.y + screenInfo.largest.height - 200);
        int time = random.nextInt(3000, 5000);
        System.out.println("创建了伴生沙发。起点：" + startX + "像素，路程：" + wayLength + "像素，高度：" + y + "像素，用时：" + time + "ms");
        return new AssociatedSofa(y, startX, wayLength, randomSofaImage().getImage(), time);
    }
    
    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {
            updateSofas();
            
            //随机移除沙发
            if (random.nextInt(0,50) == 0) {
                AssociatedSofa sofa = associations.get(random.nextInt(0, associations.size()));
                sofa.stop();
                System.out.println("随机停止了一个沙发，目前沙发数：" + associations.size());
            }
            
            //清理已经运动完毕的沙发
            associations.forEach(s -> {
                if (s.isStopped) {
                    associations.remove(s);
                    System.out.println("移除了一个沙发，目前沙发数：" + associations.size());
                }
            });
            
            //补充新的沙发
            while (associations.size() < ASSOCIATED_SOFA_COUNT) {
                AssociatedSofa sofa = randomAssociatedSofa();
                sofa.start();
                associations.add(sofa);
                System.out.println("补充了一个沙发，目前沙发数：" + associations.size());
            }
            
            if (isFinished) return;
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.exit(0);
            }
        }
    }
    
    private void updateSofas() {
        //绘制阴影，让旧的图像产生拖尾
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.035f));
        graphics.fillRect(0, 0, screenInfo.largest.width, screenInfo.largest.height);
        
        //切换画笔
        graphics.setComposite(AlphaComposite.SrcOver);
        
        //更新沙发
        mainSofa.update(graphics);
        associations.forEach(s -> s.update(graphics));
        
        //重新绘制
        panel.repaint();
    }
    
    private ImageIcon randomSofaImage() {
        String colorName = switch (random.nextInt(0,9)) {
            case 0 -> "red";
            case 1 -> "orange";
            case 2 -> "yellow";
            case 3 -> "light_green";
            case 4 -> "green";
            case 5 -> "indigo";
            case 6 -> "blue";
            case 7 -> "pink";
            default -> "purple";
        };
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofas/" + colorName + "_sofa.png")));
    }
    
    private ScreenInfo getScreenInfo() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        
        int minX = 0, minY = 0, maxX = 0, maxY = 0, minHeight = Integer.MAX_VALUE, medY = 0;
        for (GraphicsDevice screen : devices) {
            Rectangle size = screen.getDefaultConfiguration().getBounds();
            
            minX = Math.min(minX, size.x);
            minY = Math.min(minY, size.y);
            maxX = Math.max(maxX, size.x + size.width);
            maxY = Math.max(maxY, size.y + size.height);
            
            if (size.height < minHeight) {
                minHeight = size.height;
                medY = size.y + (size.height / 2);
            }
        }
        
        return new ScreenInfo(
                new Rectangle(minX, minY, maxX - minX, maxY - minY),
                medY,
                maxX - minX
        );
    }
    
    private void startMusic() throws Exception {
        music.open(AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource("/sofa_away.wav"))));
        music.start();
    }
}
