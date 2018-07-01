package com.nii.soot.core;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用检查的基础类
 * @author wzj
 * @create 2018-07-01 16:35
 **/
public abstract class BasicChecker implements IChecker
{
    /**
     * 检查的时候，要排除的包名
     */
    protected static List<String> excludePackagesList = new ArrayList<String>();

    /**
     * apk路径
     */
    protected String apkPath = "H:\\JAVA\\Soot\\apk\\app-debug.apk";

    /**
     * android jar路径
     */
    protected String jarsPath = "D:\\AndroidSDK\\platforms";

    static
    {
        excludePackagesList.add("java.");
        excludePackagesList.add("android.");
        excludePackagesList.add("javax.");
        excludePackagesList.add("android.support.");
        excludePackagesList.add("sun.");
        excludePackagesList.add("com.google.");
    }

    /**
     * 初始化soot配置
     */
    private void initSootConfig()
    {
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_output_format(Options.output_format_jimple);
        String androidJarPath = Scene.v().getAndroidJarPath(jarsPath, apkPath);

        List<String> pathList = new ArrayList<String>();
        pathList.add(apkPath);
        pathList.add(androidJarPath);

        Options.v().set_process_dir(pathList);
        Options.v().set_force_android_jar(androidJarPath);
        Options.v().set_keep_line_number(true);
        Options.v().set_process_multiple_dex(true);

        Options.v().set_wrong_staticness(Options.wrong_staticness_ignore);
        Options.v().set_exclude(excludePackagesList);

        Scene.v().loadNecessaryClasses();
        PackManager.v().runPacks();
    }

    /**
     * 是否是例外的包名
     * @param sootClass 当前的类
     * @return 检查结果
     */
    protected boolean isExcludeClass(SootClass sootClass)
    {
        if (sootClass.isPhantom())
        {
            return true;
        }

        String packageName = sootClass.getPackageName();
        for (String exclude : excludePackagesList)
        {
            if (packageName.startsWith(exclude))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 分析
     */
    public void analyze()
    {
        initSootConfig();
        checker();
    }
}
