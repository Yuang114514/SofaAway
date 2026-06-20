package cn.yuang2714.sofaaway;

import java.awt.*;

public class AssociatedSofa {
    final int y, startingX, wayLength, usingTime;
    int x;
    final Image icon;
    long startingTime;
    boolean isStopped = false;
    
    public AssociatedSofa(int y, int startingX, int wayLength, Image icon ,int usingTime) {
        this.x = startingX;
        this.y = y;
        this.startingX = startingX;
        this.wayLength = wayLength;
        this.icon = icon;
        this.startingTime = -1;
        this.usingTime = usingTime;
    }
    
    public void update(Graphics2D graphics) {
        if (startingTime == -1) return;
        if (isStopped) return;
        //计算进度
        double progress = (double) (System.currentTimeMillis() - startingTime) / usingTime;
        if (progress > 1) {
            stop();
            return;
        }
        x = (int) (startingX + (wayLength * progress));
        
        graphics.drawImage(icon, x, y, null);
    }
    
    public void start() {
        startingTime = System.currentTimeMillis() + 1500;
    }
    
    public void stop() {
        isStopped = true;
    }
}
