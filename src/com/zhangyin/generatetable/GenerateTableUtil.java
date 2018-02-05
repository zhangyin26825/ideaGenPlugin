package com.zhangyin.generatetable;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.zhangyin.directory.manage.DirectoryUtil;
import com.zhangyin.generatetable.impl.ColumnFieldConvertImpl;
import com.zhangyin.generatetable.impl.MysqlJavaConvertImpl;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.ColumnInfo;
import com.zhangyin.jdbc.JdbcUtil;
import com.zhangyin.jdbc.TableSql;
import com.zhangyin.search.ClassSearch;
import com.zhangyin.search.FindGeneratorTableUtil;
import org.apache.xmlbeans.XmlLanguage;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateTableUtil {


    public static MysqlJavaConvert convert =new MysqlJavaConvertImpl();

    public static ColumnFieldConvert columnFieldConvert=new ColumnFieldConvertImpl();

    /**
     * 生成表
     * @param tableSql
     */
    public static void generateTable(TableSql tableSql){
        JdbcUtil jdbcUtil=new JdbcUtil();
        List<ColumnInfo> columnInfos = jdbcUtil.queryColumns(tableSql.getTableName());
        //生成实体类
        createPo(tableSql,columnInfos);
        //生成Table类
        createTable(tableSql,columnInfos);
        //判断是否是复合主键，如果是  生成主键类
        if(isCompositePrimaryKey(columnInfos)){
            createCompositePrimaryKey(tableSql,columnInfos);
        }
        //创建DAO
        createDao(tableSql,columnInfos);
        //创建Mapper
        createMapper(tableSql,columnInfos);
        //创建DAOImpl
        createDaoImpl(tableSql,columnInfos);
        //生成MybatisXml
        createXmp(tableSql,columnInfos);
        //生成建表SQl
        createSQl(tableSql);

    }

    private static void createSQl(TableSql tableSql) {
        JdbcUtil jdbcUtil=new JdbcUtil();
        String filename="create_table_"+tableSql.getTableName();
        String tableSql1 = jdbcUtil.createTableSql(tableSql.getTableName())+";";

        PsiFile fileFromText = PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(filename + ".sql", PlainTextLanguage.INSTANCE, tableSql1);
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getSqlDiretory().add(fileFromText);
        });

    }

    private static void createXmp(TableSql tableSql, List<ColumnInfo> columnInfos) {
        String poClass=convert.convertClassName(tableSql.getTableName());
        String className = poClass+"Mapper";
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        stringBuffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        stringBuffer.append("<mapper namespace=\""+DirectoryUtil.getMapperPackage()+"."+className+"\">\n");
        stringBuffer.append("<resultMap id=\"resultMap\" type=\""+DirectoryUtil.getPoPackage()+"."+poClass+"\">\n");
        for (ColumnInfo columnInfo : columnInfos) {
            String s = columnFieldConvert.convertMybatisXml(columnInfo, convert);
            stringBuffer.append(s);
        }
        stringBuffer.append("</resultMap>\n");
        stringBuffer.append("</mapper>\n");

        PsiFile fileFromText = PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className + ".xml", XMLLanguage.INSTANCE, stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getXmlMapperDirectory().add(fileFromText);
        });
    }

    private static void createDaoImpl(TableSql tableSql, List<ColumnInfo> columnInfos) {
        String poClass=convert.convertClassName(tableSql.getTableName());
        String className = poClass+"DaoImpl";
        boolean composite = isCompositePrimaryKey(columnInfos);
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getDaoImplPackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import "+DirectoryUtil.getPoPackage()+"."+poClass+";\n");
        stringBuffer.append("import "+DirectoryUtil.getDaoPackage()+"."+poClass+"Dao;\n");
        stringBuffer.append("import "+DirectoryUtil.getMapperPackage()+"."+poClass+"Mapper;\n");
        if(composite){
            stringBuffer.append("import com.maqv.mybatis.core.dao.impl.MultipleIdDaoImpl;\n");
        }else{
            stringBuffer.append("import com.maqv.mybatis.core.dao.impl.SingleIdDaoImpl;\n");
        }
        stringBuffer.append("import org.springframework.stereotype.Repository;\n");
        stringBuffer.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        stringBuffer.append("import com.maqv.mybatis.core.dao.mapper.Mapper;\n");
        stringBuffer.append("\n");
        stringBuffer.append("/**\n");
        stringBuffer.append(" * Dao for "+tableSql.getTableName()+"\n");
        stringBuffer.append("*\n");
        stringBuffer.append(" * @author Ben.Ma <xiaobenma020@gmail.com>\n");
        stringBuffer.append(" */\n");
        stringBuffer.append("@Repository\n");
        stringBuffer.append("public class "+className+" extends ");
        if(composite){
            stringBuffer.append(" MultipleIdDaoImpl");
        }else{
            stringBuffer.append(" SingleIdDaoImpl");
        }
        stringBuffer.append("<"+poClass+"> implements "+poClass+"Dao {\n");
        stringBuffer.append("\n");
        stringBuffer.append("@Autowired\n");
        stringBuffer.append("private "+poClass+"Mapper mapper;\n");

        stringBuffer.append("\n");
        stringBuffer.append("@Override\n");
        stringBuffer.append("public Mapper<"+poClass+"> getMapper() {\n");
        stringBuffer.append("return mapper;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("\n");
        stringBuffer.append("@Override\n");
        stringBuffer.append("public Class<"+poClass+"> getPOClazz() {\n");
        stringBuffer.append("return "+poClass+".class;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiDaoImplDirectory().add(fileFromText);
        });
    }

    private static void createMapper(TableSql tableSql, List<ColumnInfo> columnInfos) {
        String poClass=convert.convertClassName(tableSql.getTableName());
        String className = poClass+"Mapper";
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getMapperPackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import com.maqv.mybatis.core.dao.mapper.Mapper;\n");
        stringBuffer.append("import "+DirectoryUtil.getPoPackage()+"."+poClass+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("public interface "+className+" extends ");
        stringBuffer.append("Mapper");
        stringBuffer.append("<"+poClass+"> {\n");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiMapperDirectory().add(fileFromText);
        });
    }

    private static void createDao(TableSql tableSql, List<ColumnInfo> columnInfos) {
        String poClass=convert.convertClassName(tableSql.getTableName());
        String className = poClass+"Dao";
        boolean composite = isCompositePrimaryKey(columnInfos);
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getDaoPackage()+";\n");
        stringBuffer.append("\n");
        if(composite){
            stringBuffer.append("import com.maqv.mybatis.core.dao.MultipleIdDao;\n");
            stringBuffer.append("import "+DirectoryUtil.getPoPackage()+"."+poClass+"Key;\n");
        }else{
            stringBuffer.append("import com.maqv.mybatis.core.dao.SingleIdDao;\n");
        }
        stringBuffer.append("import "+DirectoryUtil.getPoPackage()+"."+poClass+";\n");
        stringBuffer.append("\n");

        stringBuffer.append("public interface "+className+" extends ");
        if(composite){
            stringBuffer.append(" MultipleIdDao");
        }else{
            stringBuffer.append(" SingleIdDao");
        }
        stringBuffer.append("<"+poClass+"> {\n");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiDaoDirectory().add(fileFromText);
        });
    }

    /**
     * 是否是复合主键
     * @return
     */
    public static boolean isCompositePrimaryKey(List<ColumnInfo> columnInfos){
        List<ColumnInfo> collect = columnInfos.stream().filter(c -> "PRI".equals(c.getColumnKey())).collect(Collectors.toList());
        if(collect.size()<2){
            return false;
        }
        return  true;
    }

    private static  void createCompositePrimaryKey(TableSql tableSql, List<ColumnInfo> columnInfos){
        String className = convert.convertClassName(tableSql.getTableName())+"Key";
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getPoPackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import com.maqv.mysql.db.DBId;\n");
        stringBuffer.append("import com.maqv.mysql.annotation.Id;\n");
        stringBuffer.append("import com.maqv.mysql.annotation.Table;\n");
        stringBuffer.append("import lombok.Getter;\n");
        stringBuffer.append("import lombok.Setter;\n");
        stringBuffer.append("\n");
        stringBuffer.append("/**\n");
        stringBuffer.append("* Entity for "+tableSql.getTableName()+"("+tableSql.getTableComment()+")\n");
        stringBuffer.append("*\n");
        stringBuffer.append("*/\n");
        stringBuffer.append("@Setter\n");
        stringBuffer.append("@Getter\n");
        stringBuffer.append("@Table(name =\""+tableSql.getTableName()+"\",alias = \""+convert.getAlias(tableSql.getTableName())+"\")\n");
        stringBuffer.append("public class "+className +" implements DBId {\n");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        List<ColumnInfo> collect = columnInfos.stream().filter(c -> "PRI".equals(c.getColumnKey())).collect(Collectors.toList());
        addFieldToPsiJavaFile(fileFromText,collect);
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiPoDirectory().add(fileFromText);
        });

    }


    /**
     * 创建Table类
     * @param tableSql
     * @param columnInfos
     */
    private static void createTable(TableSql tableSql, List<ColumnInfo> columnInfos) {
        String className = convert.convertClassName(tableSql.getTableName())+"Table";
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getTablePackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import com.maqv.mysql.db.DBColumn;\n");
        stringBuffer.append("import com.maqv.mysql.db.DBTable;\n");
        stringBuffer.append("\n");
        stringBuffer.append("public interface "+className+" {\n");
        stringBuffer.append("\n");
        stringBuffer.append(" DBTable TABLE_NAME = new DBTable(\""+tableSql.getTableName()+"\",\""+convert.getAlias(tableSql.getTableName())+"\");\n");
        stringBuffer.append("\n");
        for (ColumnInfo columnInfo : columnInfos) {
            stringBuffer.append("DBColumn "+columnInfo.getColumnName()+" = new DBColumn(TABLE_NAME,\""+columnInfo.getColumnName()+"\");\n");
        }
        stringBuffer.append("\n");
        stringBuffer.append("}");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiTableDirectory().add(fileFromText);
        });

    }

    private static void createPo(TableSql tableSql,List<ColumnInfo> columnInfos){
        //生成 idea类的 java对象类
        PsiJavaFile javaFile = createPoJavaFile(tableSql);
        //增加属性
        addFieldToPsiJavaFile(javaFile,columnInfos);
        //写进文件
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(javaFile);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(javaFile);
            DirectoryUtil.getPsiPoDirectory().add(javaFile);
        });
        //这个表已经生成了实体类，把它加入到已经生成实体类的列表中
        FindGeneratorTableUtil.getGeneratedTable().add(tableSql);
    }

    private static void addFieldToPsiJavaFile(PsiJavaFile javaFile,List<ColumnInfo> columnInfos){
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(GlobalClass.getProject());
        PsiJavaParserFacade parserFacade = javaPsiFacade.getParserFacade();
        PsiJavaDocumentedElement psiJavaDocumentedElement = getPsiJavaDocumentedElement(javaFile);
        for (ColumnInfo columnInfo : columnInfos) {
            if(columnInfo.getColumnName().endsWith("enum")){

                PsiJavaFile enumJavaFile = createEnumJavaFile(javaFile, columnInfo);
                PsiImportList psiImportList = getPsiImportList(javaFile);
                String enumJavaClassName = convert.convertClassName(columnInfo.getColumnName());
                String s = columnFieldConvert.convertDesColumnInfo(columnInfo, convert);
                StringBuffer sb=new StringBuffer(s);
                sb.append("private "+enumJavaClassName+" "+convert.convertField(columnInfo.getColumnName())+";\n");

                PsiField fieldFromText = parserFacade.createFieldFromText(sb.toString(), null);
                WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {

                    PsiImportStatement importStatement = javaPsiFacade.getElementFactory().createImportStatement(enumJavaFile.getClasses()[0]);
                    psiImportList.add(importStatement);
                    psiJavaDocumentedElement.add(fieldFromText);
                });
            }else{
                String field = columnFieldConvert.convertFullColumnInfo(columnInfo, convert);
                PsiField fieldFromText = parserFacade.createFieldFromText(field, null);
                WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
                    psiJavaDocumentedElement.add(fieldFromText);
                });
            }
        }
    }

    private static PsiJavaDocumentedElement getPsiJavaDocumentedElement(PsiJavaFile javaFile){
        PsiElement[] children = javaFile.getChildren();
        for (PsiElement child : children) {
            if(child instanceof PsiJavaDocumentedElement){
                return (PsiJavaDocumentedElement)child;
            }
        }
        return null;
    }

    private static PsiImportList  getPsiImportList(PsiJavaFile javaFile){
        PsiElement[] children = javaFile.getChildren();
        for (PsiElement child : children) {
            if(child instanceof PsiImportList){
                return (PsiImportList)child;
            }
        }
        return null;
    }

    /**
     * 创建实体类
     * @param tableSql
     * @return
     */
    private static PsiJavaFile createPoJavaFile(TableSql tableSql){
        String className = convert.convertClassName(tableSql.getTableName());
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getPoPackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import com.maqv.mysql.annotation.Column;\n");
        stringBuffer.append("import com.maqv.mysql.annotation.Id;\n");
        stringBuffer.append("import com.maqv.mysql.annotation.Table;\n");
        stringBuffer.append("import lombok.Getter;\n");
        stringBuffer.append("import lombok.Setter;\n");
        stringBuffer.append("\n");
        stringBuffer.append("/**\n");
        stringBuffer.append("* Entity for "+tableSql.getTableName()+"("+tableSql.getTableComment()+")\n");
        stringBuffer.append("*\n");
        stringBuffer.append("*/\n");
        stringBuffer.append("@Setter\n");
        stringBuffer.append("@Getter\n");
        stringBuffer.append("@Table(name =\""+tableSql.getTableName()+"\",alias = \""+convert.getAlias(tableSql.getTableName())+"\")\n");
        stringBuffer.append("public class "+className +" {\n");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        return  fileFromText;
    }

    /**
     * 创建枚举类
     * @param javaFile
     * @param columnInfo
     * @return
     */
    private static PsiJavaFile  createEnumJavaFile(PsiJavaFile javaFile,ColumnInfo columnInfo){

        String className = convert.convertClassName(columnInfo.getColumnName());
        String fullclasspath=DirectoryUtil.getTypePackage()+"."+className;

        PsiClass javaClass = ClassSearch.getJavaClass(fullclasspath);
        if(javaClass!=null){
            return (PsiJavaFile)javaClass.getContainingFile();
        }
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getTypePackage()+";\n");
        stringBuffer.append("\n");
        stringBuffer.append("import com.maqv.mybatis.converter.CommonEnum;\n");
        stringBuffer.append("\n");
        stringBuffer.append("/**\n");
        stringBuffer.append("* 数据库字段转换生成枚举类\n");
        stringBuffer.append("*\n");
        stringBuffer.append("*/\n");
        stringBuffer.append("\n");

        stringBuffer.append("public enum "+className +"  implements CommonEnum<"+className+">{\n");
        stringBuffer.append("\n");
        stringBuffer.append(";\n");
        stringBuffer.append("\n");
        stringBuffer.append(className+"(int value){\n");
        stringBuffer.append("this.value = value;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("\n");
        stringBuffer.append("private int value;\n");
        stringBuffer.append("\n");
        stringBuffer.append("@Override\n");
        stringBuffer.append("public int getValue() {\n");
        stringBuffer.append("return value;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("\n");
        stringBuffer.append(" public static "+className+" get(Integer value) {\n");
        stringBuffer.append(" if (null == value) {\n");
        stringBuffer.append("return null;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("for ("+className+" status : values()) {\n");
        stringBuffer.append("if (value.equals(status.getValue())) {\n");
        stringBuffer.append("return status;\n");
        stringBuffer.append("}\n");
        stringBuffer.append("}\n");
        stringBuffer.append(" return null;\n");
        stringBuffer.append(" }\n");
        stringBuffer.append(" }\n");
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText(className+".java", JavaLanguage.INSTANCE,stringBuffer.toString());
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());
            styleManager.optimizeImports(fileFromText);
            CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);
            DirectoryUtil.getPsiTypeDirectory().add(fileFromText );
        });
        return fileFromText;
    }



}
