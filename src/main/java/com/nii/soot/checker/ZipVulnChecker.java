package com.nii.soot.checker;

import com.nii.soot.core.BasicChecker;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * zip目录遍历攻击检测
 *
 * @author wzj
 * @create 2018-07-04 21:54
 **/
public class ZipVulnChecker extends BasicChecker
{
    /**
     * ZipEntry 的getName()方法签名
     */
    private static final String ENTRY_GET_NAME_SIGNATURE = "<java.util.zip.ZipEntry: java.lang.String getName()>";

    /**
     * ZipEntry类
     */
    private static final String ENTRY_CLASS = "java.util.zip.ZipEntry";

    /**
     * getCanonicalPath()方法签名
     */
    private static final String GET_CANONICAL_FILE_SIGNATURE = "<java.io.File: java.lang.String getCanonicalPath()>";

    /**
     * contains方法签名
     */
    private static final String CONTAINS_SIGNATURE = "<java.lang.String: boolean contains(java.lang.CharSequence)>";

    /**
     * 具体的检测方法
     */
    public void checker()
    {
        for (SootClass sootClass : Scene.v().getApplicationClasses())
        {
            //判断是否是虚方法，是否是否是接口
            if (sootClass.isPhantom() || sootClass.isInterface() || isExcludeClass(sootClass))
            {
                continue;
            }

            for (SootMethod sootMethod : sootClass.getMethods())
            {
                if (!sootMethod.hasActiveBody())
                {
                    continue;
                }

                //判断是否含有ZipEntry类
                if (!isContainZipEntryClass(sootMethod.getActiveBody()))
                {
                    continue;
                }

                checkZipVuln(sootClass,sootMethod);
            }
        }
    }

    private boolean isContainZipEntryClass(Body body)
    {
        List<Local> localList = new ArrayList<Local>();
        localList.addAll(body.getLocals());
        localList.addAll(body.getParameterLocals());

        for (Local local : localList)
        {
            if (ENTRY_CLASS.equals(local.getType().toString()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 检测是否有zip遍历漏洞，判断规则为：
     * 如果调用了getName()方法获取解压文件路径，如果接下来有如下一种判断则认为没有漏洞
     * 1、调用了File的getCanonicalFile()方法获取绝对路径
     * 2、判断该路径是否包含 .. 字符串
     *
     * @param sootClass  sootClass
     * @param sootMethod sootMethod
     */
    private void checkZipVuln(SootClass sootClass, SootMethod sootMethod)
    {
        Body body = sootMethod.getActiveBody();
        Stmt targetStmt = null;

        //是否找到了getName() api
        boolean isFindGetNameApi = false;

        //是否有漏洞
        boolean isZipVul = true;

        for (Unit unit : body.getUnits())
        {
            Stmt stmt = (Stmt) unit;

            //判断是否是一条调用语句
            if (!stmt.containsInvokeExpr())
            {
                continue;
            }

            //获取调用语句的方法签名
            String methodSignature = stmt.getInvokeExpr().getMethod().getSignature();
            if (ENTRY_GET_NAME_SIGNATURE.equals(methodSignature))
            {
                isFindGetNameApi = true;
                targetStmt = stmt;
            }

            if (isFindGetNameApi)
            {
                //判断是否调用File的getCanonicalFile()方法
                if (GET_CANONICAL_FILE_SIGNATURE.equals(methodSignature))
                {
                    isZipVul = false;
                    break;
                }

                //判断是否调用了contains方法，并且第一个参数值为 ..
                if (CONTAINS_SIGNATURE.equals(methodSignature))
                {
                    InvokeExpr invokeExpr = stmt.getInvokeExpr();
                    Value value = invokeExpr.getArg(0);
                    if (value instanceof StringConstant && ((StringConstant) value).value.startsWith(".."))
                    {
                        isZipVul = false;
                        break;
                    }
                }
            }
        }

        if (isFindGetNameApi && isZipVul)
        {
            System.out.println("***************************************");
            System.out.println("This apk has zip vulnerability");
            System.out.println(sootClass.getName());
            System.out.println(sootMethod.getSubSignature());
            System.out.println(targetStmt.getJavaSourceStartLineNumber());
        }
    }

    public static void main(String[] args)
    {
        new ZipVulnChecker().analyze();
    }
}
