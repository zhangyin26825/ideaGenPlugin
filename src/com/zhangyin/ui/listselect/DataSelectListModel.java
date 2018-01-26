package com.zhangyin.ui.listselect;


import javax.swing.*;
import java.util.List;

public class DataSelectListModel extends AbstractListModel<String> {

    private List<String> list;

    public DataSelectListModel(List<String> list) {
        this.list = list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String getElementAt(int index) {
        return list.get(index);
    }
}
