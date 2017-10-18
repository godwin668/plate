package com.gaocy.plate.controller;

import com.gaocy.plate.util.DataUtil;
import com.gaocy.plate.util.Plate2LogoRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("plate")
public class XinController {
	
	private static final Logger logger = LoggerFactory.getLogger(XinController.class);

	@ResponseBody
	@RequestMapping("logo.init")
	public Map<String, String> init(HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "urls", required = true) String urls) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String[] urlArr = urls.split(",");
			for (String url : urlArr) {
				Plate2LogoRunner.plate2LogoAsync(url);
			}
			result.put("code", "1");
			result.put("desc", "success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("desc", e.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("logo.json")
	public Map<String, String> logo (HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			boolean ret = Plate2LogoRunner.plate2Logo(url);
			if (ret) {
				result.put("code", "1");
				result.put("desc", "success");
			} else {
				result.put("code", "0");
				result.put("desc", "fail");
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("desc", e.getMessage());
		}
		return result;
	}

	@RequestMapping("logo.jpg")
	public void jpg (HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) {
		try {
			boolean ret = Plate2LogoRunner.plate2Logo(url);
			if (ret) {
				FileUtils.copyFile(new File(DataUtil.getDestPathByUrl(url)), resp.getOutputStream());
				return;
			} else {
				logger.info("【LOGO.JPG】× 下载文件出错：未成功转换图片[{}]", url);
			}
		} catch (Exception e) {
			logger.info("【LOGO.JPG】× 下载文件出错：[{}]->[{}]", e.getMessage());
			e.printStackTrace();
		}
	}
}