package com.njp.android.wallhaven.utils;

import com.njp.android.wallhaven.bean.ImageInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 复制文件的工具类
 */

public class FileUtil {

    public static boolean CopyFile(String sourcePath, String targetPath, String targetName) {
        File source = new File(sourcePath);
        File path = new File(targetPath);

        if (!source.exists()) {
            throw new RuntimeException("Source not exist");
        }

        if (!path.exists()) {
            path.mkdirs();
        }
        File target = new File(path, targetName);
        if (target.exists()) {
            target.delete();
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(target));
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isFinish(String targetPath, String targetName) {
        File path = new File(targetPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File target = new File(path, targetName);
        if (target.exists()) {
            return true;
        }
        return false;
    }

}
