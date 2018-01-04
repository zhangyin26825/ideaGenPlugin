package com.zhangyin.init;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

public class GlobalClass {

    private static AnActionEvent e;

    public static void init(AnActionEvent event){
        e=event;
    }

    public static Project getProject(){
        return e.getData(PlatformDataKeys.PROJECT);
    }
}
