package com.zhangyin.jdbc;

import com.zhangyin.mysqlconfig.MySqlPersistent;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtil {

    MySqlPersistent.MySqlConfig mySqlConfig;

    public JdbcUtil(MySqlPersistent.MySqlConfig mySqlConfig) {
        this.mySqlConfig = mySqlConfig;
    }

    public JdbcUtil() {
        this.mySqlConfig=MySqlPersistent.getMySqlConfig();
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
            String sql="select table_name,table_comment from information_schema.tables where  table_schema=? and  table_type='base table'";
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mySqlConfig.getTableSchema());
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

    public Map<String,Integer> queryJdbcType(String tableName){
        String sql="select * from "+tableName;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = rSet.getMetaData();
            Map<String,Integer> result=new HashMap<>();
            int columnCount = metaData.getColumnCount();
            for(int i=1;i<=columnCount;i++){
                String columnName = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                result.put(columnName,columnType);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ColumnInfo>  queryColumns(String tableName){

        String sql="select * from  information_schema.COLUMNS where TABLE_SCHEMA=? and TABLE_NAME=? order by ORDINAL_POSITION";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mySqlConfig.getTableSchema());
            preparedStatement.setString(2, tableName);
            ResultSet rSet = preparedStatement.executeQuery();

            Map<String, Integer> stringIntegerMap = queryJdbcType(tableName);

            List<ColumnInfo> result=new ArrayList<>(rSet.getRow());
            while(rSet.next()){
                ColumnInfo columnInfo=new ColumnInfo();

                String column_name = rSet.getString("COLUMN_NAME");
                columnInfo.setColumnName(column_name);

                Integer integer = stringIntegerMap.get(column_name);
                columnInfo.setJdbcType(integer);

                int ordinal_position = rSet.getInt("ORDINAL_POSITION");
                columnInfo.setOrdinalPosition(ordinal_position);

                String is_nullable = rSet.getString("IS_NULLABLE");
                columnInfo.setIsNullable(is_nullable);

                String data_type = rSet.getString("DATA_TYPE");
                columnInfo.setDataType(data_type);

                Object character_maximum_length = rSet.getObject("CHARACTER_MAXIMUM_LENGTH");
                if(character_maximum_length!=null){
                    columnInfo.setCharacterMaximumLength((BigInteger) character_maximum_length);
                }

                Object character_octet_lengrh = rSet.getObject("CHARACTER_OCTET_LENGTH");
                if(character_octet_lengrh!=null){
                    columnInfo.setCharacteOctetLength((BigInteger) character_octet_lengrh);
                }

                Integer numeric_precision = rSet.getObject("NUMERIC_PRECISION", Integer.class);
                columnInfo.setNumericPercision(numeric_precision);

                Integer numeric_scale = rSet.getObject("NUMERIC_SCALE", Integer.class);
                columnInfo.setNumericScale(numeric_scale);

                String column_type = rSet.getString("COLUMN_TYPE");
                columnInfo.setColumnType(column_type);

                String column_key = rSet.getString("COLUMN_KEY");
                columnInfo.setColumnKey(column_key);


                String extra = rSet.getString("EXTRA");
                columnInfo.setExtra(extra);

                String column_comment = rSet.getString("COLUMN_COMMENT");
                columnInfo.setColumnComment(column_comment);

                result.add(columnInfo);
            }


            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String  createTableSql(String tableName){
        String sql="show create table "+tableName;

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rSet = preparedStatement.executeQuery();
            while(rSet.next()){
                String string = rSet.getString(2);
                return string;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  return  null;

    }

}
