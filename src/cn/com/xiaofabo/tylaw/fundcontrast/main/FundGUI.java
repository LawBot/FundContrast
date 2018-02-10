package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils3;
import cn.com.xiaofabo.tylaw.fundcontrast.util.GenerateCompareDoc;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.*;

/**
 * Created on @ 18.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class FundGUI extends javax.swing.JFrame {

    // Variables declaration - do not modify

    String type1 = "（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    String type2 = "（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第2号——指数型证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    String type3 = "（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第3号——证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    String type4 = "（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第4号——货币市场基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private JFileChooser chooser;

    /**
     * Creates new form FundGUI
     */
    public FundGUI() {
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FundGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FundGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FundGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FundGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FundGUI().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<String>();
        jComboBox2 = new javax.swing.JComboBox<String>();

        setTitle("Fund clause contrast. A proud product from Xiaofabo");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("公司");
        jLabel1.setFont(new Font("Serif", Font.PLAIN, 24));
        jLabel2.setText("类型");
        jLabel2.setFont(new Font("Serif", Font.PLAIN, 24));
        jTextField1.setText("file...");
        jTextField2.setText("path...");
        jButton1.setText("比较的合同");
        jButton1.setFont(new Font("Serif", Font.PLAIN, 24));
        jButton2.setText("条文的路径");
        jButton2.setFont(new Font("Serif", Font.PLAIN, 24));
        jButton3.setText("生成条文");
        jButton3.setFont(new Font("Serif", Font.PLAIN, 24));
        jButton3.setFont(jButton3.getFont().deriveFont(Font.BOLD));

        jButton1.addActionListener(new java.awt.event.ActionListener() {
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
                switch (chooser.showOpenDialog(FundGUI.this)) {
                    case JFileChooser.APPROVE_OPTION:
                        jTextField1.setText(chooser.getSelectedFile().getAbsolutePath());
                        break;
                }
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (chooser == null) {
                    chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(true);
                }
                switch (chooser.showOpenDialog(FundGUI.this)) {
                    case JFileChooser.APPROVE_OPTION:
                        jTextField2.setText(String.valueOf(chooser.getCurrentDirectory()));
                        break;
                }
            }
        });

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templateDoc = jTextField1.getText();
                try {
                    switch (String.valueOf(jComboBox2.getSelectedItem())) {
                        case "股票混合型":
                            templateDoc = STANDARD_TYPE_STOCK;
                            break;
                        case "指数型":
                            templateDoc = STANDARD_TYPE_INDEX;
                            break;
                        case "债券型":
                            templateDoc = STANDARD_TYPE_BOND;
                            break;
                        default:
                            templateDoc = STANDARD_TYPE_MONETARY;
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                String contractPath = jTextField1.getText();

                DocProcessor dp = new DocProcessor(templateDoc);
                try {
                    dp.readText(templateDoc);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                FundDoc fd = dp.process();

                DocProcessor dp2 = new DocProcessor(contractPath);
                try {
                    dp2.readText(contractPath);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                FundDoc fd2 = dp2.process();
                String title = "《" + dp2.getNameForGenDoc() + "》";
                String txt = dp2.getTextForGenDoc() + "募集申请材料之" + title;
                try {
                    switch (String.valueOf(jComboBox2.getSelectedItem())) {
                        case "股票混合型":
                            txt += type1;
                            break;
                        case "指数型":
                            txt += type2;
                            break;
                        case "债券型":
                            txt += type3;
                            break;
                        default:
                            txt += type4;
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                // generate compared doc
                GenerateCompareDoc genDoc = new GenerateCompareDoc();
                // get patchDtoList
                List<PatchDto> patchDtoList = null;
                CompareUtils3 compareUtils3 = new CompareUtils3();
				try {
					patchDtoList = compareUtils3.getPatchDtoList(templateDoc, contractPath);
					System.out.println(patchDtoList);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

                try {
                    String genDocPath = jTextField2.getText();
                    genDoc.generate(title, txt, patchDtoList, genDocPath);
                    JOptionPane.showMessageDialog(null, "成功生成对照表！");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"工银瑞信", "华夏基金", "九泰基金"}));
        jComboBox1.setFont(new Font("Serif", Font.PLAIN, 24));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"股票混合型", "指数型", "债券型", "货币型"}));
        jComboBox2.setFont(new Font("Serif", Font.PLAIN, 24));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(69, 69, 69))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
        );

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
}