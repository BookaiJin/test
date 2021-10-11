package me.bokai.utils;

import com.fr.third.org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2021/10/11
 */
public class DownloadURL {
    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            String folder = "/Users/bokai/work/Fanruan/never down/智能运维趋势/";
            File filesFolder = new File(folder);
            if (filesFolder.isDirectory()) {
                File[] files = filesFolder.listFiles();
                assert files != null;
                for (File file : files) {
                    if (!file.isDirectory()) {
                        String treasureFile = file.getAbsolutePath() + "folder/";
                        Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8).forEach(
                                url -> {
                                    String res = downloadFromUrl(url, treasureFile);
                                }
                        );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String downloadFromUrl(String url, String dir) {

        try {
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            System.out.println(dir + fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        int suffixIndex = url.lastIndexOf("?");
        if (index > 0) {
            name = url.substring(index + 1, suffixIndex);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }
}