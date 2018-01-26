package com.zhangyin.jdbc;

import java.math.BigInteger;

public class ColumnInfo {


    private String columnName;

    private Integer ordinalPosition;

    private String isNullable;

    private String dataType;

    private BigInteger characterMaximumLength;

    private BigInteger characteOctetLength;

    private Integer numericPercision;

    private Integer numericScale;

    private String columnType;

    private String columnKey;

    private String extra;

    private String columnComment;

    private Integer jdbcType;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public BigInteger getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(BigInteger characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public BigInteger getCharacteOctetLength() {
        return characteOctetLength;
    }

    public void setCharacteOctetLength(BigInteger characteOctetLength) {
        this.characteOctetLength = characteOctetLength;
    }

    public Integer getNumericPercision() {
        return numericPercision;
    }

    public void setNumericPercision(Integer numericPercision) {
        this.numericPercision = numericPercision;
    }

    public Integer getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(Integer numericScale) {
        this.numericScale = numericScale;
    }

    public Integer getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(Integer jdbcType) {
        this.jdbcType = jdbcType;
    }
}
