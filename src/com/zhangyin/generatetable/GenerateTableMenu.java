package com.zhangyin.generatetable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.TableSql;
import com.zhangyin.search.FindGeneratorTableUtil;
import com.zhangyin.ui.listselect.ListSelect;
import com.zhangyin.ui.listselect.ListValue;
import com.zhangyin.ui.listselect.SelectedCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateTableMenu extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        GlobalClass.init(e);
        List<TableSql> allNotGeneratorTable = FindGeneratorTableUtil.getAllNotGeneratorTable();
        SelectedCallback selectedCallback=new SelectedCallback() {
            @Override
            public void handleSelectValue(ListValue listValue) {
                if(listValue instanceof  TableSql){
                    TableSql tableSql = (TableSql) listValue;
                    GenerateTableUtil.generateTable(tableSql);
                }

            }
        };
        List<ListValue> listValues=new ArrayList<>(allNotGeneratorTable);
        ListSelect dataListSelect=new ListSelect(listValues,selectedCallback);
        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(dataListSelect.getPanel1(), null);
        componentPopupBuilder.setProject(GlobalClass.getProject());
        componentPopupBuilder.setMinSize(new Dimension(200,400));
        JBPopup jbPopup = componentPopupBuilder.createPopup();
        jbPopup.showInBestPositionFor(e.getDataContext());
        dataListSelect.setJbPopup(jbPopup);
    }
}
