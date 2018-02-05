package com.zhangyin.init;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.zhangyin.search.FindGeneratorTableUtil;

public class GlobalClass {

    private static AnActionEvent e;

    public static void init(AnActionEvent event){
        e=event;
        FindGeneratorTableUtil.initGeneratedTable();
    }

    public static Project getProject(){
        return e.getData(PlatformDataKeys.PROJECT);
    }
}
