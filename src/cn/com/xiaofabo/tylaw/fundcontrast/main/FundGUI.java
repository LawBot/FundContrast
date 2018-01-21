package cn.com.xiaofabo.tylaw.fundcontrast.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class FundGUI extends JFrame {
    private static Logger logger = Logger.getLogger(FundGUI.class.getName());

    public FundGUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PropertyConfigurator.configure("log.properties");
                logger.info("Inside FundGUI run()");
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("生成条文");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new MyPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setSize(800, 200);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new FundGUI();
    }

    public class MyPane extends JPanel {

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

        public MyPane() {
            logger.info("Inside MyPane, setting flowlayout");
            setLayout(new GridLayout(0, 2, 1, 3));

            logger.info("Inside FundGUI run()");
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
                    logger.info("Inside action performer, set input doc path");

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

                    switch (chooser.showOpenDialog(MyPane.this)) {
                        case JFileChooser.APPROVE_OPTION:
                            logger.info("Input file path: " + chooser.getSelectedFile().getAbsolutePath());
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
                    logger.info("Inside action performer, set output doc path");

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

                    switch (chooser.showOpenDialog(MyPane.this)) {
                        case JFileChooser.APPROVE_OPTION:
                            logger.info("Output file path: " + chooser.getSelectedFile().getAbsolutePath());
                            outputPath.setText(chooser.getSelectedFile().getAbsolutePath());
                            break;
                    }
                }
            });
            JLabel label = new JLabel();
            add(label);
            btnSubmit = new JButton("生成条文");
            add(btnSubmit);
            btnSubmit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.info("Inside action performer, generating target doc...");

                    //TODO
                }
            });
        }
    }
}
