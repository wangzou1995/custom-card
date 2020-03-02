package com.ywwl.customcard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static String getFilePath(){
        String folder = System.getProperty("java.io.tmpdir");
        String fileName = UUID.randomUUID().toString().replaceAll("-","");
        File file = new File(folder+fileName);
        file.mkdirs();
        return folder+fileName;
    }
    public static Boolean  zipFiles(File[] srcFiles, File zipFile) {
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            if (srcFiles.length == 0) {
                return false;
            }
            {
                // 实例化 FileOutputStream 对象
                fileOutputStream = new FileOutputStream(zipFile);
                // 实例化 ZipOutputStream 对象
                zipOutputStream = new ZipOutputStream(fileOutputStream);
                // 创建 ZipEntry 对象
                ZipEntry zipEntry = null;
                // 遍历源文件数组
                for (int i = 0; i < srcFiles.length; i++) {
                    // 将源文件数组中的当前文件读入 FileInputStream 流中
                    fileInputStream = new FileInputStream(srcFiles[i]);
                    // 实例化 ZipEntry 对象，源文件数组中的当前文件
                    zipEntry = new ZipEntry(srcFiles[i].getName());
                    zipOutputStream.putNextEntry(zipEntry);
                    // 该变量记录每次真正读的字节个数
                    int len;
                    // 定义每次读取的字节数组
                    byte[] buffer = new byte[1024];
                    while ((len = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                }
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                fileInputStream.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static File[] getFiles(String path) {
        File[] files = null;
        File file = new File(path);
        if (file.exists()) {

            files = file.listFiles();

        }
        return files;
    }
    public static void downloadFile (String path, HttpServletResponse response) {
        if (FileUtil.zipFiles(FileUtil.getFiles(path), new File(path + File.separator + "download.zip"))) {
            try {
                // path是指欲下载的文件的路径。
                File file = new File(path + File.separator + "download.zip");
                // 取得文件名。
                String filename = file.getName();
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(path + File.separator + "download.zip"));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                // 设置response的Header

                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.addHeader("Content-Length", "" + file.length());

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException ex) {
                logger.error("下载失败" + ex);
            }
        }

    }
}
