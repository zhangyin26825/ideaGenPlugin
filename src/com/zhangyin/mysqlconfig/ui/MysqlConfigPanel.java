package com.zhangyin.mysqlconfig.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.zhangyin.mysqlconfig.MySqlPersistent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MysqlConfigPanel extends  JPanel{


    JBPopup jbPopup;

    public MysqlConfigPanel(){
        createUIComponents();
    }



    private void createUIComponents() {

        MySqlPersistent.MySqlConfig mySqlConfig = MySqlPersistent.getMySqlConfig();
        GridLayout gridLayout=new GridLayout(5,2);
        this.setLayout(gridLayout);


        this.add(new JLabel("sql驱动",JLabel.CENTER));
        JTextField sqlDriver=new JTextField();
        sqlDriver.setText(mySqlConfig.getDriverName());
        this.add(sqlDriver);


        this.add(new JLabel("sql连接",JLabel.CENTER));
        JTextField sqlUrl=new JTextField(40);
        sqlUrl.setText(mySqlConfig.getJdbcURL());
        this.add(sqlUrl);


        this.add(new JLabel("sql用户名",JLabel.CENTER));
        JTextField sqlUsername=new JTextField();
        sqlUsername.setText(mySqlConfig.getUsername());
        this.add(sqlUsername);


        this.add(new JLabel("sql密码",JLabel.CENTER));
        JTextField sqlPassword=new JTextField();
        sqlPassword.setText(mySqlConfig.getPassword());
        this.add(sqlPassword);


        JButton save=new JButton("保存");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String text = sqlDriver.getText();
                mySqlConfig.setDriverName(text);

                String text1 = sqlUrl.getText();
                mySqlConfig.setJdbcURL(text1);

                String text2 = sqlUsername.getText();
                mySqlConfig.setUsername(text2);

                String text3 = sqlPassword.getText();
                mySqlConfig.setPassword(text3);

                MySqlPersistent.saveMySqlConfig(mySqlConfig);
                close();

            }
        });
        this.add(save);

        JButton exit=new JButton("退出");
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });
        this.add(exit);
    }

    private void  close(){
        jbPopup.cancel();
    }

    public void setJbPopup(JBPopup jbPopup) {
        this.jbPopup = jbPopup;
    }


}
