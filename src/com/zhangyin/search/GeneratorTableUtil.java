package com.zhangyin.search;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.jdbc.JdbcUtil;
import com.zhangyin.jdbc.TableSql;
import com.zhangyin.mysqlconfig.MySqlPersistent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询已经生成过实体类的表
 */
public class GeneratorTableUtil {

    public static String Table="com.maqv.mysql.annotation.Table";


    public static PsiDirectory tablePackage;

    /**
     * 根据 包含包名的完整类名   查找所有使用过这个类，java类
     * @param fullpathClassName
     * @return
     */
    private static List<PsiJavaFile> search(String fullpathClassName) {
        List<PsiJavaFile> result = new ArrayList<>();
        PsiClass javaclass = getJavaClass(fullpathClassName);
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

    private static PsiClass  getJavaClass(String fullpathClassName){
        JavaPsiFacade instance = JavaPsiFacade.getInstance(GlobalClass.getProject());
        PsiClass javaclass = instance.findClass(fullpathClassName, GlobalSearchScope.allScope(GlobalClass.getProject()));
        return javaclass;
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
        List<PsiJavaFile> search = search(Table);
        initTablePackage(search);
        List<TableSql> collect = search.stream().map(javaFile -> {
            return getTableSql(javaFile);
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 初始化 实体类所在的包名
     * @param search
     */
    private static void initTablePackage(List<PsiJavaFile> search){
        Map<PsiDirectory, Long> collect1 = search.stream().map(PsiJavaFile::getParent).collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        Integer count=0;
        for (Map.Entry<PsiDirectory, Long> psiDirectoryLongEntry : collect1.entrySet()) {
            if(psiDirectoryLongEntry.getValue().intValue()>count.intValue()){
                tablePackage=psiDirectoryLongEntry.getKey();
                count=psiDirectoryLongEntry.getValue().intValue();
            }
        }

    }

    /**
     * 获取所有 未生成实体类的表
     * @return
     */
    public static List<TableSql> getAllNotGeneratorTable(){
        JdbcUtil jdbcUtil=new JdbcUtil(MySqlPersistent.getMySqlConfig());
        List<TableSql> tableSqls = jdbcUtil.queryAllTableName();
        List<TableSql> generatedTableSql = GeneratorTableUtil.getGeneratedTableSql();
        tableSqls.removeAll(generatedTableSql);
        return tableSqls;
    }
}
