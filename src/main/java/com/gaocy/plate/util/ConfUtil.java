/*
 * Copyright (c) I Doc View. 北京卓软在线信息技术有限公司. All rights reserved.
 * 项目名称：I Doc View在线文档预览系统
 * 文件名称：ConfUtil.java
 * Date：20160821
 * Author: Godwin<godwin668@gmail.com>
 */

package com.gaocy.plate.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfUtil {

    private static Logger logger = LoggerFactory.getLogger(ConfUtil.class);

    public static Configuration config;

    static {
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(params.properties().setEncoding("UTF-8")
                                    .setFileName("conf.properties"));
            config = builder.getConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConf(String confFileName) {
        try {
            Parameters params = new Parameters();
            // Read data from this file
            File propertiesFile = new File(confFileName);
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(params.fileBased().setFile(propertiesFile));
            Configuration config = builder.getConfiguration();
            // config contains all properties read from the file
            return config;
        } catch(Exception e) {
            // loading of the configuration file failed
            e.printStackTrace();
            return null;
        }
    }

    public static Configuration getConf() {
        return config;
    }

    public static String getProperty(String key) {
        return (String) config.getProperty(key);
    }

    @Deprecated
    public static String getPropertyFromFile(String confFileName, String key) {
        try {
            ClassLoader classLoader = ConfUtil.class.getClassLoader();
            File confFile = new File(classLoader.getResource(confFileName).getFile());
            if (!confFile.isFile()) {
                logger.error("[GET CONF FROM FILE ERROR] " + confFileName + " - " + key);
                throw new Exception("[GET CONF FROM FILE ERROR] " + confFileName + " - " + key);
            }
            Properties props = new Properties();
            props.load(new FileInputStream(confFile));
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("version: " + getProperty("version"));
        System.out.println(getProperty("data.dir"));
    }
}