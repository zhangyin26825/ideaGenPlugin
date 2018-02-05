package com.zhangyin.search;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;
import com.zhangyin.directory.manage.DirectoryUtil;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.JdbcUtil;
import com.zhangyin.jdbc.TableSql;
import com.zhangyin.mysqlconfig.MySqlPersistent;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询已经生成过实体类的表
 */
public class FindGeneratorTableUtil {

    private  static String Table="com.maqv.mysql.annotation.Table";

    private static List<TableSql>  generatedTable;

    /**
     * 根据 包含包名的完整类名   查找所有使用过这个类，java类
     * @param fullpathClassName
     * @return
     */
    private static List<PsiJavaFile> search(String fullpathClassName) {
        List<PsiJavaFile> result = new ArrayList<>();
        PsiClass javaclass = ClassSearch.getJavaClass(fullpathClassName);
        Query<PsiReference> search = ReferencesSearch.search(javaclass, GlobalSearchScope.allScope(GlobalClass.getProject()));
        Set<PsiJavaFile> set=new HashSet<>();
        search.forEach(p->{
            PsiElement element = p.getElement();
            PsiFile containingFile = element.getContainingFile();
            if(containingFile instanceof  PsiJavaFile){
                set.add((PsiJavaFile)containingFile);
            }
        });
        result.addAll(set);
        return result;
    }


    private static TableSql getTableSql(PsiJavaFile javaFile){
        PsiClass psiClass = javaFile.getClasses()[0];
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if(annotation.getQualifiedName().equals(Table)){
                PsiAnnotationMemberValue name = annotation.findAttributeValue("name");
                String text = name.getText();
                TableSql tableSql = new TableSql(text);
                return tableSql;
            }
        }
        return null;
    }

    private static  List<TableSql>  getGeneratedTableSql(){
        if(generatedTable!=null){
            return generatedTable;
        }
        initGeneratedTable();
        return generatedTable;
    }

    /**
     * 初始化所有已经生成实体类的表
     */
    public static void  initGeneratedTable(){
        List<PsiJavaFile> search = search(Table);
        initTablePackage(search);
        generatedTable = search.stream().map(javaFile -> {
            return getTableSql(javaFile);
        }).collect(Collectors.toList());
    }

    /**
     * 初始化 实体类所在的包名
     * @param search
     */
    private static void initTablePackage(List<PsiJavaFile> search){
        if(DirectoryUtil.isInited()){
            return;
        }
        Map<String, Long> collect = search.stream().map(p -> p.getPackageName()).filter(StringUtils::isNotEmpty).collect(Collectors.groupingBy(j -> j, Collectors.counting()));
        Integer packageCount=0;
        String tablepackage=null;
        for(Map.Entry<String,Long> packageString:collect.entrySet()){
            if(packageString.getValue().intValue()>packageCount.intValue()){
                tablepackage=packageString.getKey();
                packageCount=packageString.getValue().intValue();
            }
        }
        DirectoryUtil.setPoPackage(tablepackage);
        Map<PsiDirectory, Long> collect1 = search.stream().map(PsiJavaFile::getParent).collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        Integer count=0;
        for (Map.Entry<PsiDirectory, Long> psiDirectoryLongEntry : collect1.entrySet()) {
            if(psiDirectoryLongEntry.getValue().intValue()>count.intValue()){
                DirectoryUtil.setPsiPoDirectory(psiDirectoryLongEntry.getKey());
                count=psiDirectoryLongEntry.getValue().intValue();
            }
        }
        DirectoryUtil.init();

    }

    /**
     * 获取所有 未生成实体类的表
     * @return
     */
    public static List<TableSql> getAllNotGeneratorTable(){
        JdbcUtil jdbcUtil=new JdbcUtil(MySqlPersistent.getMySqlConfig());
        List<TableSql> tableSqls = jdbcUtil.queryAllTableName();
        List<TableSql> generatedTableSql = FindGeneratorTableUtil.getGeneratedTableSql();
        Set<String> generateTables = generatedTableSql.stream().map(TableSql::getTableName).collect(Collectors.toSet());
        List<TableSql> result=new ArrayList<>(tableSqls.size()-generatedTableSql.size());
        for (TableSql tableSql : tableSqls) {
            if(!generateTables.contains(tableSql.getTableName())){
                result.add(tableSql);
            }
        }
        return result;
    }

    public static String getTable() {
        return Table;
    }


    public static List<TableSql> getGeneratedTable() {
        return generatedTable;
    }
}
