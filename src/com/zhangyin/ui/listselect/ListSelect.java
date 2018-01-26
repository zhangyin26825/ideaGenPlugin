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

public class ListSelect {
    private JTextField textField1;
    private JPanel panel1;
    private JButton button1;
    private JList<String> list1;



    private JBPopup jbPopup;
    private List<ListValue> dataList;
    private ListValue selectValue;




    public ListSelect(List<ListValue> dataList,SelectedCallback selectedCallback) {


        this.dataList = dataList;
        List<String> collect = dataList.stream().map(l -> l.getStringValue()).collect(Collectors.toList());
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
        textField1.enableInputMethods(true);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
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
        button1.setText("退出");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jbPopup.cancel();
            }
        });

    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public JBPopup getJbPopup() {
        return jbPopup;
    }

    public void setJbPopup(JBPopup jbPopup) {
        this.jbPopup = jbPopup;
    }
}
