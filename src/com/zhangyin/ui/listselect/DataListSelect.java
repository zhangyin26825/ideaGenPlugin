package com.zhangyin.ui.listselect;

import com.intellij.openapi.ui.popup.JBPopup;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class DataListSelect extends JPanel{

    private JTextField textField1;
    private JButton exitButton;
    private JList<String> list1;
    private JScrollBar scrollBar1;


    private JBPopup jbPopup;
    private List<ListValue> dataList;
    private ListValue selectValue;

    private void initComponent(){
        textField1=new JTextField();
        exitButton=new JButton("退出");
        list1=new JList<>();
        scrollBar1=new JScrollBar();
        scrollBar1.add(list1);
        this.setLayout(new BorderLayout());
        this.add("North", textField1);
        this.add("Center", scrollBar1);
        this.add("South", exitButton);
    }


    public DataListSelect(List<ListValue> dataList,SelectedCallback selectedCallback) {
        initComponent();

        this.dataList = dataList;
        List<String> collect = dataList.stream().map(l -> l.getStringValue()).collect(Collectors.toList());
        System.out.println("选择列表的长度为"+collect.size());
        DataSelectListModel dataSelectListModel=new DataSelectListModel(collect);
        list1.setModel(dataSelectListModel);

        list1.setCellRenderer(new DefaultListCellRenderer());
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedValue = list1.getSelectedValue();
                selectValue = dataList.stream().filter(l -> l.getStringValue().equals(selectedValue)).findFirst().get();
                jbPopup.cancel();
                selectedCallback.handleSelectValue(selectValue);
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("输入框文本内容变更"+textField1.getText());
                if(StringUtils.isNotEmpty(textField1.getText())){
                    List<String> collect1 = dataList.stream().map(l -> l.getStringValue()).filter(t -> t.contains(textField1.getText())).collect(Collectors.toList());
                    DataSelectListModel dataSelectListModel=new DataSelectListModel(collect1);
                    list1.setModel(dataSelectListModel);
                    list1.repaint();
                }else{
                    List<String> collect = dataList.stream().map(l -> l.getStringValue()).collect(Collectors.toList());
                    DataSelectListModel dataSelectListModel=new DataSelectListModel(collect);
                    list1.setModel(dataSelectListModel);
                    list1.repaint();
                }
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jbPopup.cancel();
            }
        });
        this.repaint();
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

    public void setJbPopup(JBPopup jbPopup) {
        this.jbPopup = jbPopup;
    }
}
