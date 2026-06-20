package deepseek_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class EnhancedTrailWindow extends JFrame {
    private LinkedList<Point> trail = new LinkedList<>();
    private Point dragOffset;
    private Point lastRecorded;
    private Timer recordTimer;
    private boolean isDragging = false;
    private static final int MAX_TRAIL = 20;
    private static final int RECORD_INTERVAL = 40; // ms
    private static final int MIN_DISTANCE = 8;     // 像素
    
    public EnhancedTrailWindow() {
        setTitle("增强拖影");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 1. 绘制残影（从旧到新）
                int size = trail.size();
                for (int i = 0; i < size; i++) {
                    float progress = (float) i / size;
                    
                    // 透明度：越旧越淡
                    int alpha = (int)(progress * 120);
                    // 大小：越旧越小
                    float scale = 0.5f + progress * 0.5f;
                    int w = (int)(getWidth() * scale);
                    int h = (int)(getHeight() * scale);
                    int x = (getWidth() - w) / 2;
                    int y = (getHeight() - h) / 2;
                    
                    // 主色
                    g2d.setColor(new Color(80, 140, 255, alpha));
                    g2d.fillRoundRect(x, y, w, h, 20, 20);
                    
                    // 叠加一层发光（更淡但更大）
                    g2d.setColor(new Color(80, 140, 255, alpha / 3));
                    g2d.fillRoundRect(x - 5, y - 5, w + 10, h + 10, 25, 25);
                }
                
                // 2. 绘制当前窗口
                g2d.setColor(new Color(80, 140, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
                String text = "拖影增强版 (" + trail.size() + ")";
                g2d.drawString(text, 50, 100);
            }
        };
        setContentPane(content);
        
        // 鼠标监听
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
                trail.clear();
                lastRecorded = null;
                isDragging = false;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                // 释放后残影逐渐消失（由 Timer 负责）
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragOffset == null) return;
                Point newLoc = new Point(
                        getX() + e.getX() - dragOffset.x,
                        getY() + e.getY() - dragOffset.y
                );
                setLocation(newLoc);
                isDragging = true;
                
                // 采样记录：距离足够远才记录
                if (lastRecorded == null ||
                        newLoc.distance(lastRecorded) >= MIN_DISTANCE) {
                    trail.add(new Point(0, 0));
                    if (trail.size() > MAX_TRAIL) trail.removeFirst();
                    lastRecorded = newLoc;
                    repaint();
                }
            }
        });
        
        // 定时记录（保底机制：即使移动慢也会记录）
        recordTimer = new Timer(RECORD_INTERVAL, e -> {
            if (isDragging && trail.size() < MAX_TRAIL) {
                // 如果采样不足，补充记录
                trail.add(new Point(0, 0));
                if (trail.size() > MAX_TRAIL) trail.removeFirst();
                repaint();
            }
        });
        recordTimer.start();
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EnhancedTrailWindow::new);
    }
}