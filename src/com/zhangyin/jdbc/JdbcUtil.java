package com.zhangyin.jdbc;

import com.zhangyin.mysqlconfig.MySqlPersistent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {

    MySqlPersistent.MySqlConfig mySqlConfig;

    public JdbcUtil(MySqlPersistent.MySqlConfig mySqlConfig) {
        this.mySqlConfig = mySqlConfig;
    }

    private  Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(mySqlConfig.getJdbcURL(), mySqlConfig.getUsername(), mySqlConfig.getPassword());
    }

    public List<TableSql> queryAllTableName(){
        try {
            String jdbcURL = mySqlConfig.getJdbcURL();
            int i = jdbcURL.indexOf("?");
            int i1 = jdbcURL.lastIndexOf("/");
            String tableSchema = jdbcURL.substring(i1+1, i);
            String sql="select table_name,table_comment from information_schema.tables where  table_schema=? and  table_type='base table'";
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableSchema);
            ResultSet rSet = preparedStatement.executeQuery();
            List<TableSql> result=new ArrayList<>(rSet.getRow());
            while(rSet.next()){
                String tableName=rSet.getString("table_name");
                String tableComment=rSet.getString("table_comment");
                TableSql tableSql = new TableSql(tableName, tableComment);
                result.add(tableSql);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
