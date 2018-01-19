package cn.com.xiaofabo.tylaw.fundcontrast.main;

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
    public FundGUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        private JButton lblInput;
        private JButton lblOutput;
        private JButton btnSubmit;
        private JTextField inputPath;
        private JTextField outputPath;
        private DefaultComboBoxModel companies = new DefaultComboBoxModel();
        private DefaultComboBoxModel type = new DefaultComboBoxModel();
        private JFileChooser chooser;

        public TestPane() {
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

            inputPath = new JTextField("path...");
            add(inputPath);
            lblInput = new JButton("选择文件");
            add(lblInput);
            lblInput.addActionListener(new ActionListener() {
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
//                            System.out.println("Input: " + chooser.getSelectedFile().getAbsolutePath());
                            inputPath.setText(chooser.getSelectedFile().getAbsolutePath());
                            break;
                    }
                }
            });

            outputPath = new JTextField("path...");
            add(outputPath);
            lblOutput = new JButton("输出路径");
            add(lblOutput);
            lblOutput.addActionListener(new ActionListener() {
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
//                            System.out.println("Output: " + chooser.getSelectedFile().getAbsolutePath());
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
