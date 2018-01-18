package cn.com.xiaofabo.tylaw.fundcontrast.main;

import javax.swing.*;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/

// An AWT program inherits from the top-level container java.awt.Frame
public class FundGUI extends JFrame implements ActionListener {

    private JLabel lblCom;
    private JLabel lblType;
    private JButton lblInput;
    private JButton lblOutput;
    private JButton btnSubmit;
    private JButton btnInput;
    private JButton btnOutput;
    private JTextField inputPath;
    private JTextField outputPath;

    private DefaultComboBoxModel companies = new DefaultComboBoxModel();
    private DefaultComboBoxModel type = new DefaultComboBoxModel();

    public FundGUI() {
        setLayout(new FlowLayout());

        lblCom = new JLabel("公司");
        add(lblCom);

        companies.addElement("工行");
        companies.addElement("久泰");
        companies.addElement("华夏");
        final JComboBox companyCombo = new JComboBox(companies);
        companyCombo.setSelectedIndex(0);
        add(companyCombo);

        lblType = new JLabel("类型");
        add(lblType);
        type.addElement("债券");
        type.addElement("混合");
        type.addElement("混合");
        type.addElement("混合");
        final JComboBox typeCombo = new JComboBox(type);
        typeCombo.setSelectedIndex(0);
        add(typeCombo);

        lblInput = new JButton("选择文件");
        add(lblInput);

        inputPath = new JTextField();
        add(inputPath);

        inputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser Chooser = new JFileChooser();
                Chooser.setMultiSelectionEnabled(true);
                Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = Chooser.showDialog(FundGUI.this, "Open/Save");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = Chooser.getSelectedFile();
                    outputPath.setText(file.getAbsolutePath());
                }
            }
        });

        lblOutput = new JButton("输出路径");
        add(lblOutput);
        outputPath = new JTextField();
        add(outputPath);
        outputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser Chooser = new JFileChooser();
                Chooser.setMultiSelectionEnabled(true);
                Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = Chooser.showDialog(FundGUI.this, "Open/Save");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = Chooser.getSelectedFile();
                    outputPath.setText(file.getAbsolutePath());
                }
            }
        });

        btnSubmit = new JButton("生成");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                System.out.println("generating file...");
            }
        });

        btnSubmit = new JButton("生成条文");
        add(btnSubmit);

        setTitle("生成条文");
        setSize(1000, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        FundGUI app = new FundGUI();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

    }
}
