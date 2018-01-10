package com.zhangyin;

import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiImportListImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.refactoring.typeCook.deductive.PsiTypeVariableFactory;
import com.intellij.util.IncorrectOperationException;
import com.zhangyin.init.ClassInfoInit;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.ui.TableSelect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GenAction extends AnAction {



    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("进入点击事件");
        GlobalClass.init(e);

        ClassInfoInit classInfoInit=new ClassInfoInit();
        classInfoInit.init();

        List<VirtualFile> controllers = classInfoInit.getControllers();
        VirtualFile virtualFile = controllers.get(0);

        PsiJavaFile file = (PsiJavaFile)PsiManager.getInstance(GlobalClass.getProject()).findFile(virtualFile).getOriginalFile();


        List<TableClass> tableClassList=new ArrayList<>();
        tableClassList.add(new TableClass("OperatingIncome"));
        tableClassList.add(new TableClass("OperatingOutcome"));
        JBPopup jbPopup=null;
        TableSelect tableSelect=new TableSelect(jbPopup,tableClassList);

        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(tableSelect.getPanel1(), null);
        componentPopupBuilder.setProject(GlobalClass.getProject());
        componentPopupBuilder.setMinSize(new Dimension(200,400));

        jbPopup = componentPopupBuilder.createPopup();
        jbPopup.showInBestPositionFor(e.getDataContext());
        tableSelect.setJbPopup(jbPopup);

//        System.out.println(file.getPackageName());
//        PsiImportList importList = file.getImportList();
//        PsiJavaCodeReferenceElement[] implicitlyImportedPackageReferences = file.getImplicitlyImportedPackageReferences();
//        for (PsiJavaCodeReferenceElement element : implicitlyImportedPackageReferences) {
//            System.out.println(element.getQualifiedName());
//            System.out.println(element.getRangeInElement());
//            System.out.println(element.getParameterList());
//            System.out.println(element.getTypeParameters());
//            System.out.println(element.getText());
//
//        }

//        PsiElementFactory.SERVICE
//        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(GlobalClass.getProject());
//
//        JavaPsiFacade instance = JavaPsiFacade.getInstance(GlobalClass.getProject());
//
//        PsiClass autowired = instance.findClass("org.springframework.beans.factory.annotation.Autowired", GlobalSearchScope.allScope(GlobalClass.getProject()));
//
//        PsiElement[] children = file.getChildren();
//
//        WriteCommandAction.runWriteCommandAction(GlobalClass.getProject(), new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    System.out.println("创建方法执行");
//                    PsiMethod methodFromText = elementFactory.createMethodFromText("public void hello(){ System.out.println(\"Hello\");}", file);
//
//                    PsiField field = elementFactory.createFieldFromText("@Autowired private PetApiController petApiController;", file);
//
//
//
//
//                    PsiImportStatement importStatement = elementFactory.createImportStatement(autowired);
//
//
//                    PsiElement[] elements = file.getChildren();
//                    for (PsiElement element : elements) {
//                        System.out.println(element.getClass());
//                        if(element instanceof PsiJavaDocumentedElement){
//                            element.add(methodFromText);
//                            element.add(field);
//                            System.out.println("新增方法成功");
//                        }
//                        if(element instanceof PsiImportListImpl){
//                            element.add(importStatement);
//                            System.out.println("新增导入成功");
//                        }
//                        if(element instanceof PsiClassImpl){
//
//                            PsiElement[] children1 = element.getChildren();
//                            for (PsiElement psiElement : children1) {
//                                System.out.println(psiElement);
//                            }
//
//                        }
//                    }
//
//
//                    String name = field.getName();
//                    System.out.println("fieldName    "+name);
//                    PsiAnnotation[] annotations = field.getAnnotations();
//                    for (PsiAnnotation annotation : annotations) {
//                        System.out.println("annotation   "+annotation.getQualifiedName());
//                        System.out.println(annotation.getParameterList());
//
//                    }
//                    System.out.println(annotations);
//
//
//                } catch (IncorrectOperationException e1) {
//                    System.out.println(e1);
//                    e1.printStackTrace();
//                }
//            }
//        });
//        System.out.println("执行完毕");


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
