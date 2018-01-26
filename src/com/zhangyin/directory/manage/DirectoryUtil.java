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
                    psiTableDirectory = (PsiDirectory) subPackage.getContainingFile();
                }
                if (subPackage.getName().equals("type")) {
                    typePackage = subPackage.getQualifiedName();
                    psiTypeDirectory = (PsiDirectory) subPackage.getContainingFile();
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
                    psiDaoDirectory = (PsiDirectory) psiPackage.getContainingFile();
                }

            }
        }
        PsiPackage[] subPackages = psiDaoPackage.getSubPackages();
        for (PsiPackage subPackage : subPackages) {
            if(subPackage.getName().equals("impl")){
                daoImplPackage=subPackage.getQualifiedName();
                psiDaoImplDirectory=(PsiDirectory) subPackage.getContainingFile();
            }
            if(subPackage.getName().equals("mapper")){
                mapperPackage=subPackage.getQualifiedName();
                psiMapperDirectory=(PsiDirectory)subPackage.getContainingFile();
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

            if(directory.getName().equals("resources")){
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



    public static boolean isInited() {
        return inited;
    }
}
