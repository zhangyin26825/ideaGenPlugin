package com.zhangyin.mysqlconfig;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.JdbcUtil;
import com.zhangyin.jdbc.TableSql;
import com.zhangyin.mysqlconfig.ui.MysqlConfigPanel;
import com.zhangyin.search.GeneratorTableUtil;

import java.awt.*;
import java.util.List;

public class MysqlConfigMenu extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        GlobalClass.init(e);




//        MysqlConfigPanel panel=new MysqlConfigPanel();
//        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, null);
//        componentPopupBuilder.setMinSize(new Dimension(100,250));
//        JBPopup jbPopup = componentPopupBuilder.createPopup();
//        jbPopup.showInBestPositionFor(e.getDataContext());
//        panel.setJbPopup(jbPopup);
    }
}
