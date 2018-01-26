package com.zhangyin.generatetable;

public interface MysqlJavaConvert {

     //表名转类名
     String  convertClassName(String tableName);

     //列名转属性名
     String  convertField(String columnName);

     //列名生成枚举类
     String convertEnum(String columnName);

     String  getAlias(String tableName);

}
