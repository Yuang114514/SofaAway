package deepseek;

import javax.swing.*;
import java.awt.*;

public class DeepSeekDemo extends JFrame {
    public DeepSeekDemo() {
        setTitle("登录界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);  // 居中显示
        
        // 主面板 - 四边留白20像素
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // 中间的表单面板（垂直布局）
        Box formBox = Box.createVerticalBox();
        
        // 用户名行
        Box userBox = Box.createHorizontalBox();
        userBox.add(new JLabel("用户名："));
        userBox.add(Box.createHorizontalStrut(10));
        JTextField userField = new JTextField(15);
        userBox.add(userField);
        userBox.add(Box.createHorizontalGlue());  // 右侧弹性空间
        
        // 密码行
        Box passBox = Box.createHorizontalBox();
        passBox.add(new JLabel("密码："));
        passBox.add(Box.createHorizontalStrut(10));
        JPasswordField passField = new JPasswordField(15);
        passBox.add(passField);
        passBox.add(Box.createHorizontalGlue());
        
        formBox.add(userBox);
        formBox.add(Box.createVerticalStrut(15));
        formBox.add(passBox);
        
        // 按钮面板（使用 FlowLayout 靠右）
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("登录");
        JButton cancelBtn = new JButton("取消");
        btnPanel.add(loginBtn);
        btnPanel.add(cancelBtn);
        
        // 组合到主面板
        mainPanel.add(formBox, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 事件处理
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            JOptionPane.showMessageDialog(this, "欢迎，" + username);
        });
        
        cancelBtn.addActionListener(e -> System.exit(0));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DeepSeekDemo().setVisible(true);
        });
    }
}