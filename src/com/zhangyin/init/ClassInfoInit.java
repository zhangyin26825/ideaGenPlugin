package com.zhangyin.init;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 初始化所有的类信息，寻找需要的class信息
 */
public class ClassInfoInit {

    List<VirtualFile> controllers;


    List<VirtualFile> services;


    List<VirtualFile> daos;


    List<VirtualFile> tables;
    
    
    public   void init(){
        System.out.println("开始初始化类信息");
        controllers=new ArrayList<>();
        services=new ArrayList<>();
        daos=new ArrayList<>();
        tables=new ArrayList<>();
        Set<VirtualFile> allJavaDir = getAllJavaDir();
        for (VirtualFile virtualFile : allJavaDir) {
            handldDir(virtualFile);
        }
        System.out.println("controller的数量"+controllers.size());
        System.out.println("services"+services.size());
        System.out.println("daos的数量"+daos.size());
        System.out.println("tables的数量"+tables.size());
    }

    /**
     * 获取所有的java目录
     * @return
     */

    public   Set<VirtualFile> getAllJavaDir(){
        Set<VirtualFile> set=new LinkedHashSet<>();
        Project project = GlobalClass.getProject();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile vFile : vFiles) {
//            System.out.println(vFile.getName()+"    "+vFile+"    "+vFile.getFileType()+"    "+vFile.getClass());
            if(vFile.getName().equals("java")){
                set.add(vFile);
            }
        }
        System.out.println("获取所有java目录的数量为"+set.size());
        return  set;
    }

    public  void  handldDir(VirtualFile dir){
        System.out.println("当前遍历的目录名称为"+dir.getName());
        VirtualFile[] children = dir.getChildren();
        System.out.println("当前遍历的目录的子元素数量为"+children.length);
        for (VirtualFile child : children) {
            if(child.isDirectory()){
                handldDir(child);
            }else{
                System.out.println(child.getName());
                if(child.getName().endsWith("Controller.java")){
                    controllers.add(child);
                }else if(child.getName().endsWith("Service.java")||child.getName().endsWith("ServiceImpl.java")){
                    services.add(child);
                }else if(child.getName().endsWith("Dao.java")||child.getName().endsWith("DaoImpl.java")){
                    daos.add(child);
                }else if(child.getName().endsWith("Table.java")){
                     tables.add(child);
                }else{

                }
            }

        }
    }


    public List<VirtualFile> getControllers() {
        return controllers;
    }

    public void setControllers(List<VirtualFile> controllers) {
        this.controllers = controllers;
    }

    public List<VirtualFile> getServices() {
        return services;
    }

    public void setServices(List<VirtualFile> services) {
        this.services = services;
    }

    public List<VirtualFile> getDaos() {
        return daos;
    }

    public void setDaos(List<VirtualFile> daos) {
        this.daos = daos;
    }

    public List<VirtualFile> getTables() {
        return tables;
    }

    public void setTables(List<VirtualFile> tables) {
        this.tables = tables;
    }
}
