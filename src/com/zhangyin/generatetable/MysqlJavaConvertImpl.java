package com.zhangyin.generatetable;

import org.apache.commons.lang.StringUtils;

public class MysqlJavaConvertImpl implements MysqlJavaConvert {

    //去掉下划线  连字符后的字母大写
    private String removeUnderline(String old) {
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = old.toCharArray();
        boolean isHyphen = false;
        for (char aChar : chars) {
            if (aChar == '_') {
                isHyphen = true;
                continue;
            }
            if (isHyphen) {
                stringBuffer.append(Character.toUpperCase(aChar));
                isHyphen = false;
            } else {
                stringBuffer.append(aChar);
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public String convertClassName(String tableName) {
        String result = tableName;
        if (tableName.startsWith("sz_")) {
            result = tableName.replaceFirst("sz_", "");
        }
        String s = removeUnderline(result);
        return StringUtils.capitalize(s);
    }

    @Override
    public String convertField(String columnName) {
        return removeUnderline(columnName);
    }

    @Override
    public String convertEnum(String columnName) {
        return StringUtils.capitalize(removeUnderline(columnName));
    }

    @Override
    public String getAlias(String tableName) {
        StringBuffer sb=new StringBuffer();
        String[] split = tableName.split("_");
        for (String s : split) {
            sb.append(s.charAt(0));
        }
        return sb.toString();
    }
}
