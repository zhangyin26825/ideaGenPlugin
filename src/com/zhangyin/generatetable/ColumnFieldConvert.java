package com.zhangyin.generatetable;

import com.intellij.psi.PsiField;
import com.zhangyin.jdbc.ColumnInfo;

public interface ColumnFieldConvert {

    /**
     * 生成完整的属性信息
     * @param columnInfo
     * @param mysqlJavaConvert
     * @return
     */
    String convertFullColumnInfo(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert);

    /**
     *  生成属性的描述信息，不包含 属性类型与 属性名称
     * @param columnInfo
     * @param mysqlJavaConvert
     * @return
     */
    String convertDesColumnInfo(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert);

    /**
     * 转换 xml的一行信息
     * @param columnInfo
     * @param mysqlJavaConvert
     * @return
     */
    String convertMybatisXml(ColumnInfo columnInfo, MysqlJavaConvert mysqlJavaConvert);

}
