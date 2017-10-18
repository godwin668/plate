package com.gaocy.plate.test;

import com.gaocy.plate.util.ConfUtil;
import java.io.File;

/**
 * Created by godwin on 2017-10-09.
 */
public class Test {
    public static void main(String[] args) {
        ClassLoader classLoader = ConfUtil.class.getClassLoader();
        File file = new File(classLoader.getResource("conf.properties").getFile());
        System.out.println();
    }
}