package cn.com.xiaofabo.tylaw.fundcontrast.main;

import org.apache.logging.log4j.Logger;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.File;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class FundGUI {
    Logger log;

    public FundGUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("生成条文");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 500);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new FundGUI();
    }

    public class TestPane extends JPanel {

        private JLabel lblCom;
        private JLabel lblType;
        private JButton btnInput;
        private JButton btnOutput;
        private JButton btnSubmit;
        private JTextField inputPath;
        private JTextField outputPath;
        private DefaultComboBoxModel companyNames = new DefaultComboBoxModel();
        private DefaultComboBoxModel productType = new DefaultComboBoxModel();
        private JFileChooser chooser;

        public TestPane() {
            setLayout(new FlowLayout());

            lblCom = new JLabel("公司");
            add(lblCom);
            companyNames.addElement("工银瑞信");
            companyNames.addElement("华夏基金");
            companyNames.addElement("九泰基金");
            final JComboBox companyCombo = new JComboBox(companyNames);
            companyCombo.setSelectedIndex(0);
            add(companyCombo);

            lblType = new JLabel("类型");
            add(lblType);
            productType.addElement("股票混合型");
            productType.addElement("指数型");
            productType.addElement("债券型");
            productType.addElement("货币型");
            final JComboBox typeCombo = new JComboBox(productType);
            typeCombo.setSelectedIndex(0);
            add(typeCombo);

            inputPath = new JTextField("path...");
            add(inputPath);
            btnInput = new JButton("选择文件");
            add(btnInput);
            btnInput.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chooser == null) {
                        chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        chooser.addChoosableFileFilter(new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                return f.isDirectory() || f.getName().toLowerCase().endsWith(".docx") || f.getName().toLowerCase().endsWith(".doc");
                            }

                            @Override
                            public String getDescription() {
                                return "Document (*.docx, *.doc)";
                            }
                        });
                    }

                    switch (chooser.showOpenDialog(FundGUI.TestPane.this)) {
                        case JFileChooser.APPROVE_OPTION:
                            System.out.println("Input: " + chooser.getSelectedFile().getAbsolutePath());
                            inputPath.setText(chooser.getSelectedFile().getAbsolutePath());
                            break;
                    }
                }
            });

            outputPath = new JTextField("path...");
            add(outputPath);
            btnOutput = new JButton("输出路径");
            add(btnOutput);
            btnOutput.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chooser == null) {
                        chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        chooser.addChoosableFileFilter(new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                return f.isDirectory() || f.getName().toLowerCase().endsWith(".docx") || f.getName().toLowerCase().endsWith(".doc");
                            }

                            @Override
                            public String getDescription() {
                                return "Document (*.docx, *.doc)";
                            }
                        });
                    }

                    switch (chooser.showOpenDialog(FundGUI.TestPane.this)) {
                        case JFileChooser.APPROVE_OPTION:
                            System.out.println("Output: " + chooser.getSelectedFile().getAbsolutePath());
                            outputPath.setText(chooser.getSelectedFile().getAbsolutePath());
                            break;
                    }
                }
            });

            btnSubmit = new JButton("生成条文");
            add(btnSubmit);
            btnSubmit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO
                }
            });
        }
    }
}
