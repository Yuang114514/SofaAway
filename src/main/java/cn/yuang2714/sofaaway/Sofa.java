package cn.yuang2714.sofaaway;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Sofa extends JFrame {
    private final Clip music = AudioSystem.getClip();
    
    public Sofa() throws Exception {
        setTitle("Sofa Away");
        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setAlwaysOnTop(true);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofa.png"))).getImage());
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {}
        };
        panel.setOpaque(false);
        
        JLabel sofa = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofa.png"))));
        panel.add(sofa, BorderLayout.CENTER);
        
        add(panel);
        setVisible(true);
    }
    
    @SuppressWarnings("BusyWait")
    public void away() throws Exception {
        /*
        坐标计算：
        已知目标x，目标时间，现在时间，起始时间，起始x
        求现在x
        解：
            计算现在走的进度：(现在时间 - 起始时间) / (目标时间 - 起始时间)
            计算现在x：起始x + (路程 * 现在进度)
        */
        
        Way way = getWay();
        System.out.println("沙发正在移动... 路程：" + way.length + "像素");
        setLocation(way.startX, way.y);//移动到起点
        
        startMusic(); //播放音乐
        Thread.sleep(1500); //等音乐唱 "So far away"
        
        //开始移动
        long startTime = System.currentTimeMillis();
        Timer timer = new Timer(10, e -> {
            long currentTime = e.getWhen();
            double progress = (double) (currentTime - startTime) / 12650;
            int currentX = (int) (way.startX + (way.length * progress));
            setLocation(currentX, way.y);
        });
        timer.start();
        
        while (true) {
            Thread.sleep(10);
            if (System.currentTimeMillis() >= startTime + 12650) {
                timer.stop();
                Thread.sleep(500);
                System.out.println("沙发移动完毕");
                return;
            }
        }
    }
    
    private record Way(int startX, int y, int length) {}
    
    private Way getWay() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        
        int smallestX = 0;
        int biggestX = 0;
        int smallestHeightY = 0;
        int smallestHeight = Integer.MAX_VALUE;
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.x < smallestX) smallestX = bounds.x;
            if (bounds.x + bounds.width > biggestX) biggestX = bounds.x + bounds.width;

            if (bounds.height < smallestHeight) {
                smallestHeight = bounds.height;
                smallestHeightY = bounds.y;
            }
        }
        
        return new Way(
                smallestX,
                 smallestHeightY + (smallestHeight / 2) - 150,
                biggestX - smallestX - 300
        );
    }
    
    private void startMusic() throws Exception {
        music.open(AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource("/sofa_away.wav"))));
        music.start();
    }
}
