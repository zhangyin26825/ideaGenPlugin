package com.zhangyin.generatetable.impl;

import com.zhangyin.generatetable.ColumnFieldConvert;
import com.zhangyin.generatetable.MysqlJavaConvert;
import com.zhangyin.jdbc.ColumnInfo;
import com.zhangyin.jdbc.JdbcTypeTranslater;
import org.apache.commons.lang.StringUtils;

public class ColumnFieldConvertImpl implements ColumnFieldConvert {

    @Override
    public String convertFullColumnInfo(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert) {

        String desColumnInfo = convertDesColumnInfo(columnInfo, mysqlJavaConvert);
        StringBuffer sb=new StringBuffer(desColumnInfo);
        sb.append("private ").append(JdbcTypeTranslater.getJavaClassName(columnInfo.getJdbcType()))
                .append(" ")
                .append(mysqlJavaConvert.convertField(columnInfo.getColumnName())).append(";\n");
        return sb.toString();
    }

    @Override
    public String convertDesColumnInfo(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert) {
        StringBuffer sb=new StringBuffer();
        sb.append("/**\n");
        sb.append("*"+columnInfo.getColumnComment()+"\n");
        sb.append("*"+columnInfo.getColumnType()+"\n");
        sb.append("*/\n");
        if(StringUtils.equals("PRI", columnInfo.getColumnKey())){
            sb.append("@Id(");
        }else{
            sb.append("@Column(");
        }
        sb.append("name=\""+columnInfo.getColumnName()+"\"");
        if(StringUtils.equals("YES", columnInfo.getIsNullable())){
            sb.append(", nullable = true");
        }
        if(StringUtils.equals("auto_increment", columnInfo.getExtra())){
            sb.append(", autoIncrease = true");
        }
        sb.append(")\n");
        return sb.toString();
    }

    @Override
    public String convertMybatisXml(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert) {
        StringBuffer sb=new StringBuffer();
        if(StringUtils.equals("PRI", columnInfo.getColumnKey())){
            sb.append("<id ");
        }else{
            sb.append("<result ");
        }
        sb.append("column=\"").append(columnInfo.getColumnName()).append("\" jdbcType=\"")
                .append(JdbcTypeTranslater.getJdbcTypeName(columnInfo.getJdbcType()))
                .append("\" property=\"")
                .append(mysqlJavaConvert.convertField(columnInfo.getColumnName()))
                .append("\" ");
        if(columnInfo.getColumnName().endsWith("enum")){
            sb.append("typeHandler=\"com.maqv.mybatis.converter.CommonEnumTypeHandler\"");
        }
        sb.append("/>\n");
        return sb.toString();
    }
}
