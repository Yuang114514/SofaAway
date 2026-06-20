package cn.yuang2714.sofaaway;

import javax.swing.*;

public class SofaAway {
    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.uiScale.enabled", "false");
        System.setProperty("sun.java2d.noddraw", "true");
        
        Sofa sofa = new Sofa();
        //sofa.away();//SOFA AWAY!!!
        
        System.exit(0);
    }
}
