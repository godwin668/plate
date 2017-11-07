package com.gaocy.plate.controller;

import com.gaocy.plate.util.BaiduAiUtil;
import com.gaocy.plate.util.DataUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("ocr")
public class OcrController {
	
	private static final Logger logger = LoggerFactory.getLogger(OcrController.class);

	/**
	 * 上传行驶证
	 *
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("vehicleLicense/upload.json")
	public Map<String, Object> vehicleLicenseUpload(
			HttpServletRequest req,
			HttpServletResponse resp,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject ret = BaiduAiUtil.vehicleLicenseRecognitionByte(file.getBytes());
			if (null != ret) {
				result.put("code", "1");
				result.put("desc", "success");
				result.put("data", ret.toMap());
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

	/**
	 * 上传车牌
	 *
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("plateLicense/upload.json")
	public Map<String, Object> plateLicenseUpload(
			HttpServletRequest req,
			HttpServletResponse resp,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject ret = BaiduAiUtil.plateLicenseRecognitionByte(file.getBytes());
			if (null != ret) {
				result.put("code", "1");
				result.put("desc", "success");
				result.put("data", ret.toMap());
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

	/**
	 * URL行驶证
	 * @param req
	 * @param resp
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("vehicleLicense/url.json")
	public Map<String, Object> vehicleLicenseUrl (HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			File file = DataUtil.downloadUrl(url);
			JSONObject ret = BaiduAiUtil.vehicleLicenseRecognition(file);
			if (null != ret) {
				result.put("code", "1");
				result.put("desc", "success");
				result.put("data", ret);
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

	/**
	 * URL车牌
	 * @param req
	 * @param resp
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("plateLicense/url.json")
	public Map<String, Object> plateLicense (HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			File file = DataUtil.downloadUrl(url);
			JSONObject ret = BaiduAiUtil.plateLicenseRecognition(file);
			if (null != ret) {
				result.put("code", "1");
				result.put("desc", "success");
				result.put("data", ret);
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
}