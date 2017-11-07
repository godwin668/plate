package com.gaocy.plate.util;

import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by godwin on 2017-11-02.
 */
public class BaiduAiUtil {

    private static Logger logger = LoggerFactory.getLogger(BaiduAiUtil.class);

    //设置APPID/AK/SK
    public static final String APP_ID = "9535188";                                              // App ID
    public static final String API_KEY = "vOqh1x0SREbzXDCqTAS0kTci";                        // Api Key
    public static final String SECRET_KEY = "PsXetGMaTGOCPddA6KOYoZ66zLbU79Xy";             // Secret Key

    public static final AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);            // 初始化一个AipOcr

    public static void main(String[] args) {
        File vehicleImg = new File("/Users/Godwin/tmp/xsz.jpg");
        System.out.println(vehicleLicenseRecognition(vehicleImg).toString());

        File plateImg = new File("/Users/Godwin/tmp/cp.jpg");
        System.out.println(plateLicenseRecognition(plateImg).toString());

    }

    /**
     * url 2 file

    /**
     * 行驶证识别
     * @param file
     * @return
     */
    public static JSONObject vehicleLicenseRecognition(File file) {
        HashMap<String, String> options = new HashMap<String, String>();
        return client.vehicleLicense(file.getAbsolutePath(), options);
    }

    /**
     * 行驶证识别
     * @param data
     * @return
     */
    public static JSONObject vehicleLicenseRecognitionByte(byte[] data) {
        HashMap<String, String> options = new HashMap<String, String>();
        return client.vehicleLicense(data, options);
    }

    /**
     * 车牌识别
     * @param file
     * @return
     */
    public static JSONObject plateLicenseRecognition(File file) {
        return client.plateLicense(file.getAbsolutePath());
    }

    /**
     * 车牌识别
     * @param data
     * @return
     */
    public static JSONObject plateLicenseRecognitionByte(byte[] data) {
        return client.plateLicense(data);
    }

}