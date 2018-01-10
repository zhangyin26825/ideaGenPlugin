package com.zhangyin.ui;

import com.zhangyin.TableClass;

import javax.swing.*;
import java.util.List;

public class TableSelectListModel extends AbstractListModel<TableClass> {

    private List<TableClass> list;

    public TableSelectListModel(List<TableClass> list) {
        this.list = list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public TableClass getElementAt(int index) {
        return list.get(index);
    }
}
