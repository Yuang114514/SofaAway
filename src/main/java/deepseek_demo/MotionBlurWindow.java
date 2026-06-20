package deepseek_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MotionBlurWindow extends JFrame {
    private BufferedImage canvas;          // 画布（保存拖尾效果）
    private Image image;                   // 要显示的图片
    private Point mouseDownLoc = null;
    private int blurAlpha = 30;            // 拖尾强度（0-255，越小拖尾越长）
    
    public MotionBlurWindow() {
        setTitle("动态模糊窗口");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);              // 无边框，更干净
        
        // 加载图片（请替换为你的图片路径）
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofa.png"))).getImage();
        
        // 初始化画布（与窗口大小一致）
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        clearCanvas();  // 初始透明
        
        // 自定义内容面板
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 将画布绘制到窗口
                g.drawImage(canvas, 0, 0, null);
            }
        });
        
        // 窗口拖动实现
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownLoc = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (mouseDownLoc != null) {
                    // 1. 移动窗口
                    Point loc = getLocation();
                    setLocation(loc.x + e.getX() - mouseDownLoc.x,
                            loc.y + e.getY() - mouseDownLoc.y);
                    
                    // 2. 更新画布（绘制拖尾）
                    updateCanvas();
                    
                    // 3. 重绘窗口
                    repaint();
                }
            }
        });
        
        setVisible(true);
    }
    
    /**
     * 更新画布：先淡化，再绘制新图片
     */
    private void updateCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        
        // 1. 用半透明黑色覆盖整个画布（实现淡化效果）
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blurAlpha / 255f));
        g2d.setColor(new Color(0, 0, 0, blurAlpha));
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // 2. 恢复不透明绘制
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // 3. 在当前位置绘制图片（填充整个窗口）
        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
        
        g2d.dispose();
    }
    
    /**
     * 清空画布（完全透明）
     */
    private void clearCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.dispose();
    }
    
    /**
     * 窗口大小变化时重新初始化画布
     */
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (canvas != null) {
            canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            clearCanvas();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MotionBlurWindow());
    }
}