package com.gaocy.plate.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by godwin on 2017-10-11.
 */
public class Plate2LogoRunner implements Callable<Boolean> {

    private static Logger logger = LoggerFactory.getLogger(Plate2LogoRunner.class);

    private static String cmdPath = ConfUtil.getProperty("cmd.plate2logo");
    private static String logoPath = ConfUtil.getProperty("logo.path");

    public static final int processorCount = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService es = Executors.newFixedThreadPool(processorCount);

    private String src;
    private String dest;

    static {
        if (!new File(cmdPath).isFile()) {
            Plate2LogoRunner.class.getClass().getClassLoader();
            File plateLogoReplacerFile = new File(Plate2LogoRunner.class.getClassLoader().getResource("comm/plateLogoReplacer.exe").getFile());
            cmdPath = plateLogoReplacerFile.getPath().replaceAll("\\\\", "/");
            logger.info("【INIT】 cmdPath: " + cmdPath);
        }
        if (!new File(logoPath).isFile()) {
            Plate2LogoRunner.class.getClass().getClassLoader();
            File plateLogoReplacerFile = new File(Plate2LogoRunner.class.getClassLoader().getResource("comm/logo.png").getFile());
            logoPath = plateLogoReplacerFile.getPath().replaceAll("\\\\", "/");
            logger.info("【INIT】 logoPath: " + logoPath);
        }
    }

    public Plate2LogoRunner(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public Boolean call() throws Exception {
        return plate2Logo(src, dest);
    }

    public Future<Boolean> submit() {
        return es.submit(this);
    }

    /**
     * plate to logo
     *
     * @param url
     * @return
     */
    public static boolean plate2Logo(String url) {
        String localPath = DataUtil.getLocalPathByUrl(url);
        String destPath = DataUtil.getDestPathByUrl(url);
        return Plate2LogoRunner.plate2Logo(localPath, destPath);
    }

    /**
     * plate to logo
     *
     * @param url
     * @return
     */
    public static void plate2LogoAsync(String url) {
        String localPath = DataUtil.getLocalPathByUrl(url);
        String destPath = DataUtil.getDestPathByUrl(url);
        Plate2LogoRunner.plate2LogoAsync(localPath, destPath);
    }

    /**
     * Plate to Logo
     *
     * @param src
     * @param dest
     * @return 1 success, 0 fail
     */
    public static boolean plate2Logo(String src, String dest) {
        if (new File(dest).isFile()) {
            logger.debug("【PLATE2LOGO】√ 目标文件已存在: " + dest);
            return true;
        }

        // 如果原始文件不存在，则先下载
        if (!new File(src).isFile()) {
            String url = DataUtil.getUrlByLocalPath(src);
            try {
                logger.debug("【PLATE2LOGO】↓ 下载文件：[{}]->[{}]", url, src);
                FileUtils.copyURLToFile(new URL(url), new File(src));
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("【PLATE2LOGO】× 下载文件出错：[{}]->[{}]", url, src);
            }
            if (!new File(src).isFile()) {
                logger.info("【PLATE2LOGO】× 未找到原始文件: " + src);
                return false;
            }
        }
        long start = System.currentTimeMillis();
        String result = CmdUtil.runWindows(cmdPath, logoPath, src, dest);
        result = result.replaceAll("\n", " ");
        Object[] argArr = new Object[] {src, dest, result, "" + (System.currentTimeMillis() - start)};
        if (null != result && result.contains("success")) {
            logger.info("【PLATE2LOGO】 √ [{}]->[{}] RET[{}] ELAPSE[{}]ms.", argArr);
            return true;
        } else {
            logger.info("【PLATE2LOGO】 × [{}]->[{}] RET[{}] ELAPSE[{}]ms.", argArr);
            return false;
        }
    }

    /**
     * Plate to Logo(Async)
     *
     * @param src
     * @param dest
     * @return 1 success, 0 fail
     */
    public static void plate2LogoAsync(String src, String dest) {
        new Plate2LogoRunner(src, dest).submit();
    }

    public static void main(String[] args) {
        try {
            System.out.println("hello...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}