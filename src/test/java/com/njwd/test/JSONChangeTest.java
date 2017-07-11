package com.njwd.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONChangeTest {

	
	public static void main(String[] args) throws IOException {
		String json1 =FileUtils.readFileToString(new File("E:\\changejson\\1.json"), "UTF-8");
		String json2 =FileUtils.readFileToString(new File("E:\\changejson\\2.json"), "UTF-8");
		JSONObject jo = JSON.parseObject(json1);
		JSONObject jo2 = JSON.parseObject(json2);
		FileUtils.writeStringToFile(new File("E:\\changejson\\1-response.json"), jo.toJSONString(),"UTF-8");
		FileUtils.writeStringToFile(new File("E:\\changejson\\2-response.json"), jo2.toJSONString(),"UTF-8");
	}
}
