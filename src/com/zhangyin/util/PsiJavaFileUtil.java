package com.zhangyin.util;

import com.intellij.psi.PsiAnnotation;


import java.util.ArrayList;
import java.util.List;

public class PsiJavaFileUtil {


    public static   List<PsiAnnotation>  searchAnnotation(PsiAnnotation[] annotations,String fullname){
        List<PsiAnnotation> result=new ArrayList<>();
        for (PsiAnnotation annotation : annotations) {
            if(annotation.getQualifiedName().equals(fullname)){
                result.add(annotation);
            }
        }
        return result;
    }
}
