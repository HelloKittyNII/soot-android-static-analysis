package com.nii.soot.checker;

import com.nii.soot.core.BasicChecker;
import soot.jimple.infoflow.android.resources.ARSCFileParser;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author wzj
 * @create 2018-07-01 21:45
 **/
public class ResourceSootChecker
{
    /**
     * apk路径
     */
    private static String apkPath = "H:\\JAVA\\Soot\\apk\\app-debug.apk";

    public static void main(String[] args) throws IOException
    {
        ARSCFileParser arscFileParser = new ARSCFileParser();
        arscFileParser.parse(apkPath);
        Map<Integer, String> globalStringPoolMap = arscFileParser.getGlobalStringPool();

      //  Map<Integer, String> stringTable = arscFileParser.stringTable;
        for (Map.Entry<Integer, String> entry : globalStringPoolMap.entrySet())
        {
            if (entry.getValue().startsWith("res/"))
            {
                continue;
            }

            System.out.println(entry.getValue());
        }
    }

}
