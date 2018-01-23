package com.zhangyin.jdbc;

public class TableSql {

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
}
