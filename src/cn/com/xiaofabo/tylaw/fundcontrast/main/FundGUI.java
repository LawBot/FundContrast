package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;
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
import java.io.IOException;
import java.util.List;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class FundGUI extends JFrame {
    private static Logger logger = Logger.getLogger(FundGUI.class.getName());
    String inputPath1 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）.doc";
    String inputPath2 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第2号——指数型证券投资基金基金合同填报指引（试行）.doc";
    String inputPath3 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第3号——证券投资基金基金合同填报指引（试行）.doc";
    String inputPath4 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第4号——货币市场基金基金合同填报指引（试行）.doc";

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
                try {
                    frame.add(new MyPane());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 400);
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

        public MyPane() throws IOException {
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

            inputPath = new JTextField("file...");
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

            // TODO
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
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
                            logger.info("Output file path: " + chooser.getCurrentDirectory());
                            outputPath.setText(String.valueOf(chooser.getCurrentDirectory()));
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

//                    String testPath = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第4号——货币市场基金基金合同填报指引（试行）.doc";
//                    String testPath2 = "data/Sample/华夏基金/货币/华夏兴金宝货币市场基金基金合同（草案） 1026.docx";
                    String templateDoc = inputPath.getText();
                    try {
                        switch (String.valueOf(typeCombo.getSelectedItem())) {
                            case "股票混合型":
                                templateDoc = inputPath1;
                                break;
                            case "指数型":
                                templateDoc = inputPath2;
                                break;
                            case "债券型":
                                templateDoc = inputPath3;
                                break;
                            default:
                                templateDoc = inputPath4;
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                    String contractPath = inputPath.getText();

                    DocProcessor dp = new DocProcessor(templateDoc);
                    try {
                        dp.readText(templateDoc);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    FundDoc fd = dp.process();
                    java.util.List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

                    DocProcessor dp2 = new DocProcessor(contractPath);
                    try {
                        dp2.readText(contractPath);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    FundDoc fd2 = dp2.process();
                    java.util.List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();
                    List<PatchDto> patchDtoList = null;
                    try {
                        patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    GenerateCompareDoc test = new GenerateCompareDoc();
                    String title = "《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》\n";
                    String txt = "九泰天辰量化新动力混合型证券投资基金募集申请材料之《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引(试行)》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
                    try {
                        String genDocPath = outputPath.getText();
                        test.generate(title, txt, patchDtoList, genDocPath);
                        System.out.println("finished");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }
}