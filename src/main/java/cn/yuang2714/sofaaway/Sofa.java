package cn.yuang2714.sofaaway;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Sofa extends JFrame {
    public Sofa() {
        setTitle("Sofa Away");
        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
            
            }
        };
        panel.setOpaque(false);
        
        JLabel sofa = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/sofa.png"))));
        panel.add(sofa, BorderLayout.CENTER);
        
        add(panel);
    }
    
    public void away() {
    
    }
    
    public static void main(String[] args) {
        new Sofa().setVisible(true);
    }
}
