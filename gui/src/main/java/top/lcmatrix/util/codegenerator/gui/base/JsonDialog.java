package top.lcmatrix.util.codegenerator.gui.base;

import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JsonDialog extends JDialog {

    private Object object2show;

    public JsonDialog(Frame owner, String title, Object object2show){
        super(owner, title, true);
        this.object2show = object2show;
        setResizable(true);
//        setLocationRelativeTo(owner);

        JPanel contentPanel = new JPanel(new GridLayout(0, 1));
        JTextArea textArea = new JTextArea();
        textArea.setMargin(new Insets(DM.dm2pix(5), DM.dm2pix(8), DM.dm2pix(5), DM.dm2pix(8)));
        textArea.setText(JSON.toJSONString(object2show, true));
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                textArea.setSelectionStart(0);
            }
        });

        contentPanel.add(textArea);
        this.setContentPane(new MyJScrollPane(contentPanel));
    }
}
