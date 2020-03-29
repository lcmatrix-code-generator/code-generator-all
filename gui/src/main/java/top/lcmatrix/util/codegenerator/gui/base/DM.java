package top.lcmatrix.util.codegenerator.gui.base;

import java.awt.*;

public class DM {

    private final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final static int DMS_OF_SCREEN = 480;
    public final static double DM = (SCREEN_SIZE.getHeight() < SCREEN_SIZE.getWidth() ? SCREEN_SIZE.getHeight() : SCREEN_SIZE.getWidth()) / DMS_OF_SCREEN;

    public static int dm2pix(double dm){
        return (int) Math.round(dm * DM);
    }

    public static double pix2dm(int pix){
        return pix / DM;
    }
}
