package com.zhangyin.directory.manage;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.zhangyin.init.GlobalClass;

/**
 * 管理 生成类所在的目录  包名之类的信息   实体类   枚举  dao  mapper
 */
public class DirectoryUtil {

    private static boolean inited=false;

    //实体类所在的包
    private static String poPackage;
    //对应的idea中的目录对象   增加java类的必须用 psiTableDirectory.add方法来实现
    private static PsiDirectory psiPoDirectory;

    //table类
    private static String tablePackage;

    private static PsiDirectory psiTableDirectory;
    //枚举类
    private static String typePackage;

    private static PsiDirectory psiTypeDirectory;

    //Dao
    private static String daoPackage;

    private static PsiDirectory psiDaoDirectory;
    //Dao实现类
    private static String daoImplPackage;

    private static PsiDirectory psiDaoImplDirectory;
    //Mapper
    private static String mapperPackage;

    private static PsiDirectory psiMapperDirectory;

    //sql文件放置的目录
    private static PsiDirectory  sqlDiretory;

    //mybatis xml放置的目录
    private static  PsiDirectory xmlMapperDirectory;


    public static void init() {
        inited=true;
        PsiPackage psiPoPackage = JavaPsiFacade.getInstance(GlobalClass.getProject()).findPackage(poPackage);
        {
            PsiPackage[] subPackages = psiPoPackage.getSubPackages();
            for (PsiPackage subPackage : subPackages) {
                if (subPackage.getName().equals("table")) {
                    tablePackage = subPackage.getQualifiedName();
                    psiTableDirectory = (PsiDirectory) subPackage.getDirectories()[0];
                }
                if (subPackage.getName().equals("type")) {
                    typePackage = subPackage.getQualifiedName();
                    psiTypeDirectory = (PsiDirectory) subPackage.getDirectories()[0];
                }
            }
        }
        PsiPackage psiDaoPackage=null;
        {
            PsiPackage parentPackage = psiPoPackage.getParentPackage();
            PsiPackage[] subPackages1 = parentPackage.getSubPackages();
            for (PsiPackage psiPackage : subPackages1) {
                if (psiPackage.getName().equals("dao")) {
                    psiDaoPackage = psiPackage;
                    daoPackage = psiPackage.getQualifiedName();
                    psiDaoDirectory = (PsiDirectory) psiPackage.getDirectories()[0];
                }

            }
        }
        PsiPackage[] subPackages = psiDaoPackage.getSubPackages();
        for (PsiPackage subPackage : subPackages) {
            if(subPackage.getName().equals("impl")){
                daoImplPackage=subPackage.getQualifiedName();
                psiDaoImplDirectory=(PsiDirectory) subPackage.getDirectories()[0];
            }
            if(subPackage.getName().equals("mapper")){
                mapperPackage=subPackage.getQualifiedName();
                psiMapperDirectory=(PsiDirectory)subPackage.getDirectories()[0];
            }

        }
        initSqlAndMybatisXmlDirectory(psiDaoPackage);
    }
    //初始化  sql  跟  mybatis xml 的目录
    private  static void  initSqlAndMybatisXmlDirectory(PsiPackage psiPackage){
        PsiPackage root=psiPackage;
        while(root.getParentPackage()!=null){
            root=root.getParentPackage();
        }
        PsiDirectory[] directories = root.getDirectories();
        for (PsiDirectory directory : directories) {

            String path = directory.getVirtualFile().getPath();

            if(directory.getName().equals("resources")&&path.contains("core/src/main")){
                sqlDiretory= directory.findSubdirectory("db").findSubdirectory("migrations");
                xmlMapperDirectory=directory.findSubdirectory("com").findSubdirectory("voffice")
                        .findSubdirectory("sz")
                        .findSubdirectory("core")
                        .findSubdirectory("v2")
                        .findSubdirectory("dao")
                        .findSubdirectory("mapper");
                break;
            }
        }
    }

    public static String getPoPackage() {
        return poPackage;
    }

    public static PsiDirectory getPsiPoDirectory() {
        return psiPoDirectory;
    }

    public static void setPsiPoDirectory(PsiDirectory psiPoDirectory) {
        DirectoryUtil.psiPoDirectory = psiPoDirectory;
    }

    public static void setPoPackage(String poPackage) {
        DirectoryUtil.poPackage = poPackage;
        System.out.println("实体类所在的包名为"+ poPackage);
    }

    public static void setInited(boolean inited) {
        DirectoryUtil.inited = inited;
    }

    public static String getTablePackage() {
        return tablePackage;
    }

    public static void setTablePackage(String tablePackage) {
        DirectoryUtil.tablePackage = tablePackage;
    }

    public static PsiDirectory getPsiTableDirectory() {
        return psiTableDirectory;
    }

    public static void setPsiTableDirectory(PsiDirectory psiTableDirectory) {
        DirectoryUtil.psiTableDirectory = psiTableDirectory;
    }

    public static String getTypePackage() {
        return typePackage;
    }

    public static void setTypePackage(String typePackage) {
        DirectoryUtil.typePackage = typePackage;
    }

    public static PsiDirectory getPsiTypeDirectory() {
        return psiTypeDirectory;
    }

    public static void setPsiTypeDirectory(PsiDirectory psiTypeDirectory) {
        DirectoryUtil.psiTypeDirectory = psiTypeDirectory;
    }

    public static String getDaoPackage() {
        return daoPackage;
    }

    public static void setDaoPackage(String daoPackage) {
        DirectoryUtil.daoPackage = daoPackage;
    }

    public static PsiDirectory getPsiDaoDirectory() {
        return psiDaoDirectory;
    }

    public static void setPsiDaoDirectory(PsiDirectory psiDaoDirectory) {
        DirectoryUtil.psiDaoDirectory = psiDaoDirectory;
    }

    public static String getDaoImplPackage() {
        return daoImplPackage;
    }

    public static void setDaoImplPackage(String daoImplPackage) {
        DirectoryUtil.daoImplPackage = daoImplPackage;
    }

    public static PsiDirectory getPsiDaoImplDirectory() {
        return psiDaoImplDirectory;
    }

    public static void setPsiDaoImplDirectory(PsiDirectory psiDaoImplDirectory) {
        DirectoryUtil.psiDaoImplDirectory = psiDaoImplDirectory;
    }

    public static String getMapperPackage() {
        return mapperPackage;
    }

    public static void setMapperPackage(String mapperPackage) {
        DirectoryUtil.mapperPackage = mapperPackage;
    }

    public static PsiDirectory getPsiMapperDirectory() {
        return psiMapperDirectory;
    }

    public static void setPsiMapperDirectory(PsiDirectory psiMapperDirectory) {
        DirectoryUtil.psiMapperDirectory = psiMapperDirectory;
    }

    public static PsiDirectory getSqlDiretory() {
        return sqlDiretory;
    }

    public static void setSqlDiretory(PsiDirectory sqlDiretory) {
        DirectoryUtil.sqlDiretory = sqlDiretory;
    }

    public static PsiDirectory getXmlMapperDirectory() {
        return xmlMapperDirectory;
    }

    public static void setXmlMapperDirectory(PsiDirectory xmlMapperDirectory) {
        DirectoryUtil.xmlMapperDirectory = xmlMapperDirectory;
    }

    public static boolean isInited() {
        return inited;
    }
}
