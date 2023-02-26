package com.kaya.ixdreader.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ContentWriteUtil {
    public static void writeContentToFile(String text,String fileName) {
        writeTrueContentToFile(text, fileName);
    }

    private static void writeTrueContentToFile(String text,String fileName) {
        String needWriteMessage = text;
        File dirsFile = new File(Constant.PATH_WRITE);
        if (!dirsFile.exists()){
            dirsFile.mkdirs();
        }
        File file = new File(dirsFile.toString(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }

        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
