package deepseek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedMove extends JFrame {
    private int targetX, targetY;
    private Timer timer;
    
    public AnimatedMove() {
        setTitle("窗口平滑移动");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 控制面板
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnTopLeft = new JButton("左上");
        JButton btnTopRight = new JButton("右上");
        JButton btnBottomLeft = new JButton("左下");
        JButton btnBottomRight = new JButton("右下");
        JButton btnCenter = new JButton("居中");
        
        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        
        btnTopLeft.addActionListener(e -> moveTo(0, 0));
        btnTopRight.addActionListener(e ->
                moveTo(screenSize.width - frameSize.width, 0));
        btnBottomLeft.addActionListener(e ->
                moveTo(0, screenSize.height - frameSize.height));
        btnBottomRight.addActionListener(e ->
                moveTo(screenSize.width - frameSize.width,
                        screenSize.height - frameSize.height));
        btnCenter.addActionListener(e ->
                moveTo((screenSize.width - frameSize.width) / 2,
                        (screenSize.height - frameSize.height) / 2));
        
        panel.add(btnTopLeft);
        panel.add(btnTopRight);
        panel.add(btnBottomLeft);
        panel.add(btnBottomRight);
        panel.add(btnCenter);
        
        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }
    
    private void moveTo(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        
        // 使用 Timer 实现平滑移动
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        timer = new Timer(10, new ActionListener() {
            private Point current = getLocation();
            
            @Override
            public void actionPerformed(ActionEvent e) {
                int dx = (targetX - current.x) / 5;
                int dy = (targetY - current.y) / 5;
                
                if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                    setLocation(targetX, targetY);
                    timer.stop();
                } else {
                    current.x += dx;
                    current.y += dy;
                    setLocation(current);
                }
            }
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnimatedMove());
    }
}