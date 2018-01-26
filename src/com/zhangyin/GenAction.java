package com.zhangyin;

import com.intellij.codeInsight.actions.ReformatCodeAction;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.zhangyin.init.ClassInfoInit;
import com.zhangyin.init.GlobalClass;
import com.zhangyin.mysqlconfig.MySqlPersistent;

import java.util.List;

public class GenAction extends AnAction {



    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("进入点击事件");
        GlobalClass.init(e);


        MySqlPersistent.MySqlConfig mySqlConfig = MySqlPersistent.getMySqlConfig();
        System.out.println("mysql用户名"+mySqlConfig.getUsername());

        mySqlConfig.setUsername("zhangyin");
        MySqlPersistent.saveMySqlConfig(mySqlConfig);

        MySqlPersistent.MySqlConfig mySqlConfig1 = MySqlPersistent.getMySqlConfig();
        System.out.println("mysql用户名"+mySqlConfig1.getUsername());


        ClassInfoInit classInfoInit=new ClassInfoInit();
        classInfoInit.init();

        List<VirtualFile> controllers = classInfoInit.getControllers();
        VirtualFile virtualFile = controllers.get(0);

        PsiJavaFile file = (PsiJavaFile)PsiManager.getInstance(GlobalClass.getProject()).findFile(virtualFile).getOriginalFile();


        PsiPackage pkg = JavaPsiFacade.getInstance(GlobalClass.getProject()).findPackage(file.getPackageName());
        PsiJavaFile fileFromText = (PsiJavaFile)PsiFileFactory.getInstance(GlobalClass.getProject()).createFileFromText("HelloWorldTest.java",JavaLanguage.INSTANCE,"package "+pkg.getQualifiedName()+";"
                +"public class  HelloWorldTest{}");



//// 用编辑器打开指定文件
//        FileEditorManager.getInstance(GlobalClass.getProject()).openTextEditor(new OpenFileDescriptor(project, virtualFile), true);




//        PsiElement[] children = fileFromText.getChildren();
//        PsiDirectory parent = file.getParent();
//        parent.add(fileFromText);
//        // 格式化代码
        CodeStyleManager.getInstance(GlobalClass.getProject()).reformat(fileFromText);

        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(GlobalClass.getProject());

        styleManager.optimizeImports(file);
//        ReformatCodeAction
        System.out.println("新增文件成功");

//        System.out.println(file.getName());
//
//        System.out.println("fing useage");
//        Query<PsiReference> search = ReferencesSearch.search(file );
//
//
//        for (PsiReference psiReference : search) {
//            PsiElement element = psiReference.getElement();
//            System.out.println(element);
//        }

//        PropertiesComponent instance = PropertiesComponent.getInstance(GlobalClass.getProject());



//        List<TableClass> tableClassList=new ArrayList<>();
//        tableClassList.add(new TableClass("OperatingIncome"));
//        tableClassList.add(new TableClass("OperatingOutcome"));
//        JBPopup jbPopup=null;
//        TableSelect tableSelect=new TableSelect(jbPopup,tableClassList);
//
//        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(tableSelect.getPanel1(), null);
//        componentPopupBuilder.setProject(GlobalClass.getProject());
//        componentPopupBuilder.setMinSize(new Dimension(200,400));
//
//        jbPopup = componentPopupBuilder.createPopup();
//        jbPopup.showInBestPositionFor(e.getDataContext());
//        tableSelect.setJbPopup(jbPopup);

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
