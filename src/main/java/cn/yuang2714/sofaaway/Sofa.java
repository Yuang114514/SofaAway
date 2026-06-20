package cn.yuang2714.sofaaway;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Sofa extends JFrame {
    private final Clip music = AudioSystem.getClip();
    private final BufferedImage drawingPane;
    private final Graphics2D graphics;
    
    public Sofa() throws Exception {
        //基本设置
        setTitle("Sofa Away");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);

        //透明背景
//        setUndecorated(true);
//        setBackground(new Color(0,0,0,0));
        
        //设置图标
        ImageIcon sofaImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofas/white_sofa.png")));
        setIconImage(sofaImage.getImage());
        
        //设置画布
        Rectangle size = getFullScreenSize();
        setSize(size.getSize());
        drawingPane = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        graphics = drawingPane.createGraphics();
        
        //清空背景
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0,0,size.width,size.height);
        
        //调整画笔模式
        graphics.setComposite(AlphaComposite.SrcOver);
        
        setVisible(true);
    }
    
    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawImage(drawingPane,0,0,null);
    }
    
    @SuppressWarnings("BusyWait")
    public void away() throws Exception {
        startMusic();
    }
    
    private Rectangle getFullScreenSize() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (GraphicsDevice screen : devices) {
            Rectangle size = screen.getDefaultConfiguration().getBounds();
            
            minX = Math.min(minX, size.x);
            minY = Math.min(minY, size.y);
            maxX = Math.max(maxX, size.x + size.width);
            maxY = Math.max(maxY, size.y + size.height);
        }
        
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
    
    private void startMusic() throws Exception {
        music.open(AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource("/sofa_away.wav"))));
        music.start();
    }
}
