package com.zhangyin;

import com.intellij.lang.StdLanguages;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.PsiShortNamesCache;
import com.zhangyin.init.ClassInfoInit;
import com.zhangyin.init.GlobalClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GenAction extends AnAction {

    JBPopup popup=null;

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("进入点击事件");
        GlobalClass.init(e);

        ClassInfoInit classInfoInit=new ClassInfoInit();
        classInfoInit.init();


        //获取项目的源码目录  一般的maven 项目 包含 src/main/java   src/main/resources 这两个目录
//        VirtualFile javaDir=null;
//        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
//        for (VirtualFile vFile : vFiles) {
//            System.out.println(vFile.getName()+"    "+vFile+"    "+vFile.getFileType()+"    "+vFile.getClass());
//            if(vFile.getName().equals("java")){
//                javaDir=vFile;
//            }
//        }


//        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();


//        PsiShortNamesCache.getInstance(project).getClassesByName()

//        FilenameIndex.getFilesByName(project, name, scope)

//        PsiFile psi = PsiManager.getInstance(project).findViewProvider(javaDir).getPsi(JavaLanguage.INSTANCE);

//        ListPopup listPopup = JBPopupFactory.getInstance().createActionGroupPopup("ListPopup Sample", actionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);


//        JPanel jPanel1=new JPanel();
//        JLabel hello = new JLabel("Hello");
//        hello.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("hello");
//                popup.cancel();
//            }
//        });
//
//        jPanel1.add(hello);
//        jPanel1.add(new JLabel("Hello1"));
//        jPanel1.add(new JLabel("Hello2"));
//        jPanel1.add(new JLabel("Hello3"));
//
//        JPanel jPanel2=new JPanel();
//
//
//        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(jPanel1, null);
//        componentPopupBuilder.setProject(project);
//        componentPopupBuilder.setMinSize(new Dimension(200,400));
//
//        popup = componentPopupBuilder.createPopup();
//        popup.showInBestPositionFor(e.getDataContext());
//        popup.



    }
}
