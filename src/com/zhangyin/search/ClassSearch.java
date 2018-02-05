package com.zhangyin.search;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.zhangyin.init.GlobalClass;

public class ClassSearch {

    public  static PsiClass getJavaClass(String fullpathClassName){
        JavaPsiFacade instance = JavaPsiFacade.getInstance(GlobalClass.getProject());
        PsiClass javaclass = instance.findClass(fullpathClassName, GlobalSearchScope.allScope(GlobalClass.getProject()));
        return javaclass;
    }
}
