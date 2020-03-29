package top.lcmatrix.util.codegenerator.gui.base;

import javax.swing.*;
import java.awt.*;

public class MyJScrollPane extends JScrollPane {

    public static final String VERTICAL = "vertical";
    public static final String HORIZONTAL = "horizontal";
    private static final int DEFAULT_SCROLL_UNIT_INCREMENT = DM.dm2pix(10);

    public MyJScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        defaultScrollUnitIncrement();
    }

    public MyJScrollPane(Component view) {
        super(view);
        defaultScrollUnitIncrement();
    }

    public MyJScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        defaultScrollUnitIncrement();
    }

    public MyJScrollPane() {
        super();
        defaultScrollUnitIncrement();
    }

    private void defaultScrollUnitIncrement(){
        setUnitIncrement(VERTICAL, DEFAULT_SCROLL_UNIT_INCREMENT);
        setUnitIncrement(HORIZONTAL, DEFAULT_SCROLL_UNIT_INCREMENT);
    }

    public void setUnitIncrement(String whichBar, int unitIncrement){
        switch (whichBar){
            case VERTICAL:
                if(getVerticalScrollBar() != null){
                    getVerticalScrollBar().setUnitIncrement(unitIncrement);
                }
                break;
            case HORIZONTAL:
                if(getHorizontalScrollBar() != null){
                    getHorizontalScrollBar().setUnitIncrement(unitIncrement);
                }
                break;
            default:

        }
    }
}
