package com.gaocy.plate.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by godwin on 2017-10-12.
 */
public class DataUtil {

    private static Logger logger = LoggerFactory.getLogger(DataUtil.class);

    private static String DATA_DIR = ConfUtil.getProperty("data.dir");

    /**
     * 获取本地相对路径
     *
     * @param url
     * @return
     */
    public static String getRelativePathByUrl(String url) {
        return url.replaceFirst("://", "__");
    }

    /**
     * 获取本地相对路径（无后缀）
     *
     * @param url
     * @return
     */
    public static String getRelativePathByUrlWithoutSuffix(String url) {
        String relativePath = getRelativePathByUrl(url);
        return relativePath.substring(0, relativePath.lastIndexOf("."));
    }

    /**
     * 获取后缀
     *
     * @param url
     * @return
     */
    public static String getSuffix(String url) {
        int indexOfDot = url.lastIndexOf(".");
        return url.substring(indexOfDot + 1);
    }

    /**
     * 获取本地路径
     *
     * @param url
     * @return
     */
    public static String getLocalPathByUrl(String url) {
        File localFile = new File(DATA_DIR, getRelativePathByUrl(url));
        if (!localFile.getParentFile().isDirectory()) {
            localFile.getParentFile().mkdirs();
        }
        return localFile.getPath().replaceAll("\\\\", "/");
    }

    /**
     * 获取本地meta路径
     *
     * @param url
     * @return
     */
    public static String getMetaPathByUrl(String url) {
        File metaFile = new File(DATA_DIR, getRelativePathByUrlWithoutSuffix(url) + ".json");
        if (!metaFile.getParentFile().isDirectory()) {
            metaFile.getParentFile().mkdirs();
        }
        return metaFile.getPath().replaceAll("\\\\", "/");
    }

    /**
     * 获取本地目标文件（打logo后）路径
     *
     * @param url
     * @return
     */
    public static String getDestPathByUrl(String url) {
        File metaFile = new File(DATA_DIR, getRelativePathByUrlWithoutSuffix(url) + "_dest." + getSuffix(url));
        if (!metaFile.getParentFile().isDirectory()) {
            metaFile.getParentFile().mkdirs();
        }
        return metaFile.getPath().replaceAll("\\\\", "/");
    }

    /**
     * 本地路径->URL
     *
     * @param localPath
     * @return
     */
    public static String getUrlByLocalPath(String localPath) {
        localPath = localPath.replaceAll("\\\\", "/");
        String relativePath = localPath.substring(localPath.indexOf(DATA_DIR) + DATA_DIR.length() + (DATA_DIR.endsWith("/") ? 0 : 1));
        return relativePath.replaceFirst("__", "://");
    }

    public static File downloadUrl(String url) {
        String localPath = getLocalPathByUrl(url);
        if (!new File(localPath).isFile()) {
            try {
                logger.debug("【DATAUTIL】↓ 下载文件：[{}]->[{}]", url, localPath);
                FileUtils.copyURLToFile(new URL(url), new File(localPath));
            } catch (Exception e) {
                logger.warn("【DATAUTIL】× 下载文件出错：[{}]->[{}]", url, localPath);
                e.printStackTrace();
            }
            if (!new File(localPath).isFile()) {
                logger.info("【DATAUTIL】× 未找到原始文件: " + localPath);
            }
        }
        return new File(localPath);
    }

    public static void main(String[] args) {
        String url = "https://c4.xinstatic.com/f2/20171012/1536/59df1b710e576715332_20.jpg";
        System.out.println("本地相对路径：\n" + getRelativePathByUrl(url));
        System.out.println("本地相对路径（无后缀）：\n" + getRelativePathByUrlWithoutSuffix(url));
        System.out.println("获取后缀：\n" + getSuffix(url));
        System.out.println("获取本地路径：\n" + getLocalPathByUrl(url));
        System.out.println("本地meta路径：\n" + getMetaPathByUrl(url));
        System.out.println("获取本地目标文件（打logo后）路径：\n" + getDestPathByUrl(url));
        System.out.println("本地路径->URL：\n" + getUrlByLocalPath(getLocalPathByUrl(url)));

    }

}