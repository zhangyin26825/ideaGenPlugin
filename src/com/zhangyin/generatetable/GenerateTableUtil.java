package com.zhangyin.generatetable;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
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
        PsiJavaFile javaFile = createJavaFile(tableSql);
    }


    public static PsiJavaFile  createJavaFile(TableSql tableSql){
        String className = convert.convertClassName(tableSql.getTableName());
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("package "+ DirectoryUtil.getTablePackage()+";\n");
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
        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), () -> {
            DirectoryUtil.getPsiTableDirectory().add(fileFromText);
        });
        return  fileFromText;
    }



}
