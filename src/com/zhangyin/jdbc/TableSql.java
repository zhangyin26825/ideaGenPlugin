package com.zhangyin.jdbc;

import com.zhangyin.ui.listselect.ListValue;
import org.apache.commons.lang.StringUtils;

public class TableSql implements ListValue{

    private String tableName;

    private String tableComment;

    public TableSql(String tableName, String tableComment) {
        this.tableName = tableName;
        this.tableComment = tableComment;
    }

    public TableSql(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getStringValue() {
        if(StringUtils.isNotEmpty(tableComment)){
            return  tableName+"("+tableComment+")";
        }
        return tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableSql tableSql = (TableSql) o;

        return tableName.equals(tableSql.tableName);
    }

    @Override
    public int hashCode() {
        return tableName.hashCode();
    }

    @Override
    public String toString() {
        return tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }
}
