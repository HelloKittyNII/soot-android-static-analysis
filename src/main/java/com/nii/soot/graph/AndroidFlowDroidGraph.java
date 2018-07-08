package com.nii.soot.graph;

import soot.Scene;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CallGraph;

/**
 * 获取android应用控制流图
 * @author wzj
 * @create 2018-07-01 21:20
 **/
public class AndroidFlowDroidGraph
{
    /**
     * apk路径
     */
    private String apkPath = "H:\\JAVA\\Soot\\apk\\app-debug.apk";

    /**
     * android jar路径
     */
    private String jarsPath = "D:\\AndroidSDK\\platforms";

    /**
     * 文件一定要有，可以为空
     */
    private String androidCallbackPath = "H:\\JAVA\\Soot\\conf\\AndroidCallbacks.txt";


    public static void main(String[] args)
    {
        //初始化soot配置
        new AndroidFlowDroidGraph().initSootConfig();

        CallGraph callGraph = Scene.v().getCallGraph();
        System.out.println(callGraph);
    }

    private SetupApplication initSootConfig()
    {
        String androidJarPath = Scene.v().getAndroidJarPath(jarsPath, apkPath);
        SetupApplication setupApplication = new SetupApplication(androidJarPath, apkPath);
        InfoflowAndroidConfiguration config = setupApplication.getConfig();
        config.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        setupApplication.setCallbackFile(androidCallbackPath);

        //构建控制流图，比较耗时
        setupApplication.constructCallgraph();

        return setupApplication;
    }
}
