package com.nii.soot.checker;

import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

import java.io.IOException;

/**
 * @author wzj
 * @create 2018-07-06 21:11
 **/
public class MenifestChecker
{
    /**
     * apk路径
     */
    private static String apkPath = "H:\\JAVA\\Soot\\apk\\clock.apk";

    public static void main(String[] args) throws IOException, XmlPullParserException
    {
        ProcessManifest processManifest = new ProcessManifest(apkPath);

        //获取包名
        System.out.println(processManifest.getManifest().getAttribute("package"));

        System.out.println(processManifest.getPermissions());
        System.out.println(processManifest.getActivities());
        System.out.println(processManifest.getServices());
        System.out.println(processManifest.getProviders());
    }
}
