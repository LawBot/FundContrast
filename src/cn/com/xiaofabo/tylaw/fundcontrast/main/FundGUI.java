package cn.com.xiaofabo.tylaw.fundcontrast.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/

// An AWT program inherits from the top-level container java.awt.Frame
public class FundGUI extends Frame implements ActionListener {
    private Label lblCom;
    private Label lblType;
    private Label lblInput;
    private Label lblOutput;
    private Button btnSubmit;

    public FundGUI() {
        setLayout(new FlowLayout());

        lblCom = new Label("公司");
        add(lblCom);
        lblType = new Label("类型");
        add(lblType);
        lblInput = new Label("选择文件");
        add(lblInput);
        lblOutput = new Label("输出路径");
        add(lblOutput);
        btnSubmit = new Button("生成条文");
        add(btnSubmit);

        setTitle("生成条文");
        setSize(750, 300);
        setVisible(true);

    }

    public static void main(String[] args) {
        FundGUI app = new FundGUI();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

    }
}
