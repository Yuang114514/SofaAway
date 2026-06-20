package deepseek_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SimpleMotionBlur extends JFrame {
    
    // ====== 核心变量 ======
    private BufferedImage canvas;   // 画布：用来保存"上一帧"的画面
    private Image image;            // 你要显示的图片
    private Point mouseDownLoc;     // 鼠标按下时的位置（用于拖动窗口）
    
    public SimpleMotionBlur() {
        // 1. 窗口基本设置
        setTitle("动态模糊 - 极简版");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);  // 去掉窗口边框，看起来更像一张"图片"
        
        // 2. 加载图片（把 "your_image.jpg" 换成你的图片路径）
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofa.png"))).getImage();
        
        // 3. 创建画布（大小和窗口一样）
        //    BufferedImage 就像一个"内存中的画板"，我们可以在上面画图
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        // 4. 清空画布（变成完全透明）
        clearCanvas();
        
        // 5. 设置窗口的内容面板（所有显示内容都在这里）
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 把画布绘制到窗口上
                g.drawImage(canvas, 0, 0, null);
            }
        });
        
        // ====== 6. 实现窗口拖动 ======
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标按下时的位置（相对于窗口）
                mouseDownLoc = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownLoc != null) {
                    // 6.1 移动窗口
                    Point loc = getLocation();  // 当前窗口在屏幕上的位置
                    setLocation(
                            loc.x + e.getX() - mouseDownLoc.x,  // 新X坐标
                            loc.y + e.getY() - mouseDownLoc.y   // 新Y坐标
                    );
                    
                    // 6.2 ★★★ 核心：更新画布（制造动态模糊效果）★★★
                    updateCanvas();
                    
                    // 6.3 重绘窗口（会调用 paintComponent，显示画布内容）
                    repaint();
                }
            }
        });
        
        // 7. 显示窗口
        setVisible(true);
    }
    
    /**
     * ★★★ 核心方法：更新画布，制造动态模糊 ★★★
     *
     * 原理：
     * 1. 先用"半透明的黑色"覆盖整个画布 → 之前的画面会变淡
     * 2. 然后在当前位置画一张清晰的图片
     * 3. 这样旧的图片就会逐渐变淡消失，形成"拖尾"
     */
    private void updateCanvas() {
        // 获取画布的绘图工具
        Graphics2D g2d = canvas.createGraphics();
        
        // ----- 第1步：用半透明黑色覆盖整个画布（淡化和残留） -----
        // 设置透明度：0.15 表示 15% 不透明，85% 透明
        // 意思就是：保留之前画面的 85%，只淡化 15%
        // 这样旧的画面不会立刻消失，而是慢慢变淡
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        g2d.setColor(new Color(0, 0, 0));  // 黑色
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // ----- 第2步：恢复不透明，画一张清晰的图片 -----
        // 设置透明度为 1.0（完全不透明）
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // 在画布的左上角(0,0)开始绘制图片，拉伸填满整个窗口
        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
        
        // 释放绘图资源
        g2d.dispose();
    }
    
    /**
     * 清空画布（变成完全透明）
     */
    private void clearCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        // CLEAR 模式会完全清除画布内容
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.dispose();
    }
    
    public static void main(String[] args) {
        // 在事件线程中启动 GUI
        SwingUtilities.invokeLater(() -> new SimpleMotionBlur());
    }
}