package com.zhangyin.generatetable;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.zhangyin.directory.manage.DirectoryUtil;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.ColumnInfo;
import com.zhangyin.jdbc.JdbcUtil;
import com.zhangyin.jdbc.TableSql;

import java.util.List;

public class GenerateTableUtil {


    public static MysqlJavaConvert convert =new MysqlJavaConvertImpl();

    /**
     * 生成表
     * @param tableSql
     */
    public static void generateTable(TableSql tableSql){
        JdbcUtil jdbcUtil=new JdbcUtil();
        List<ColumnInfo> columnInfos = jdbcUtil.queryColumns(tableSql.getTableName());
        createPo(tableSql,columnInfos);

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

    }

    private static void addFieldToPsiJavaFile(PsiJavaFile javaFile,List<ColumnInfo> columnInfos){
        for (ColumnInfo columnInfo : columnInfos) {


        }
    }

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



}
