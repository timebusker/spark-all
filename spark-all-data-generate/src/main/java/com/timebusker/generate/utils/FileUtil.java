package com.timebusker.generate.utils;

import com.google.common.base.Preconditions;
import com.sun.org.apache.bcel.internal.generic.FSUB;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static final String ENCODE_GBK = "GBK";

    public static final String ENCODE_UTF8 = "UTF-8";

    public static final String RENAME_FILE_SUFFIX = "_done";

    /**
     * 遍历目录文件
     *
     * @param path
     * @param suffix
     * @return
     */
    public static List<File> listFiles(String path, String suffix) throws Exception {
        File root = new File(path);
        if (!root.exists() || !root.isDirectory()) {
            throw new Exception("输入文件路径：" + path + " 有误，请确保路径存在且是目录！");
        }
        File[] files = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(suffix)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        return Arrays.asList(files);
    }

    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static String data;

    public static String readLineFile(File file, long index) throws Exception {
        return readLineFile(file, index, ENCODE_UTF8);
    }

    public static String readLineFile(File file, long index, String encode) throws Exception {
        InputStreamReader in = new InputStreamReader(new FileInputStream(file), encode);
        bufferedReader = new BufferedReader(in);
        long count = 0;
        do {
            data = bufferedReader.readLine();
            if (data == null) {
                break;
            }
            count++;
        } while (count <= index);
        bufferedReader.close();
        return data;
    }

    public static void writeFile(File file, String data) throws Exception {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.write(data);
        bufferedWriter.write("\n");
        bufferedWriter.close();
    }

    public static boolean renameFile(File file) {
        String parent = file.getParent();
        String name = file.getName();
        File renameFile = new File(parent + "/" + name + RENAME_FILE_SUFFIX);
        file.renameTo(renameFile);
        if (!file.exists() && renameFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean createPath(String path) {
        Preconditions.checkNotNull(path);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return true;
    }
}
