package com.zhangyin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.zhangyin.TableClass;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class TableSelect {
    private JTextField textField1;
    private JPanel panel1;
    private JButton exitButton;
    private JList<TableClass> list1;
    private JScrollBar scrollBar1;
    private JBPopup jbPopup;
    private List<TableClass> tableClassList;


    public TableSelect(JBPopup bPopup, List<TableClass> tableClassList) {
        this.jbPopup=bPopup;
        this.tableClassList=tableClassList;
        TableSelectListModel tableSelectListModel = new TableSelectListModel(tableClassList);
        list1.setModel(tableSelectListModel);
        list1.setCellRenderer(new DefaultListCellRenderer());
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableClass selectedValue = list1.getSelectedValue();
                System.out.println("选中的值为"+selectedValue.getName());
                System.out.println("jbPopup   "+jbPopup);
                jbPopup.cancel();
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("输入框文本内容变更"+textField1.getText());
                if(StringUtils.isNotEmpty(textField1.getText())){
                    List<TableClass> collect = tableClassList.stream()
                            .filter(t -> t.getName().contains(textField1.getText())).collect(Collectors.toList());
                    System.out.println("变化的表数量的值为"+collect.size());
                    TableSelectListModel tableSelectListModel = new TableSelectListModel(collect);
                    list1.setModel(tableSelectListModel);
                }else{
                    TableSelectListModel tableSelectListModel = new TableSelectListModel(tableClassList);
                    list1.setModel(tableSelectListModel);
                }
            }
        });

//        textField1.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                System.out.println("输入框文本内容变更"+textField1.getText());
//                    if(StringUtils.isNotEmpty(textField1.getText())){
//                        List<TableClass> collect = tableClassList.stream()
//                                .filter(t -> t.getName().contains(textField1.getText())).collect(Collectors.toList());
//                        TableClass[] objects =(TableClass[]) collect.toArray();
//                        list1.setListData(objects);
//                    }
//            }
//        });



        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jbPopup.cancel();
            }
        });
    }

    public  void init(){

        ListModel jListModel =  new DefaultComboBoxModel(new String[] { "张三", "李四" });  //数据模型
        list1.setModel(jListModel);

        panel1.repaint();
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("TableSelect");
//        TableSelect tableSelect = new TableSelect();
//        tableSelect.init();
//        frame.setContentPane(tableSelect.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public void setJbPopup(JBPopup jbPopup) {
        this.jbPopup = jbPopup;
    }
}
