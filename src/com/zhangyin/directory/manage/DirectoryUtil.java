package com.zhangyin.directory.manage;

import com.intellij.psi.PsiDirectory;

/**
 * 管理 生成类所在的目录  包名之类的信息   实体类   枚举  dao  mapper
 */
public class DirectoryUtil {

    private static boolean inited=false;

    //实体类所在的包
    private static String  tablePackage;
    //对应的idea中的目录对象   增加java类的必须用 psiTableDirectory.add方法来实现
    private static PsiDirectory psiTableDirectory;


    public static void init() {
        inited=true;
    }



    public static String getTablePackage() {
        return tablePackage;
    }

    public static void setTablePackage(String tablePackage) {
        DirectoryUtil.tablePackage = tablePackage;
        System.out.println("实体类所在的包名为"+tablePackage);
    }

    public static PsiDirectory getPsiTableDirectory() {
        return psiTableDirectory;
    }

    public static void setPsiTableDirectory(PsiDirectory psiTableDirectory) {
        DirectoryUtil.psiTableDirectory = psiTableDirectory;
    }

    public static boolean isInited() {
        return inited;
    }
}
