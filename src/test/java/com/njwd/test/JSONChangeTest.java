package com.njwd.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

public class JSONChangeTest {

	
	public static void main(String[] args) throws IOException {
		String json1 =FileUtils.readFileToString(new File("E:\\changejson\\1.json"), "UTF-8");
		String json2 =FileUtils.readFileToString(new File("E:\\changejson\\2.json"), "UTF-8");
		Map jo = JSON.parseObject(json1,LinkedHashMap.class,Feature.OrderedField);
		Map jo2 = JSON.parseObject(json2,LinkedHashMap.class,Feature.OrderedField);
		FileUtils.writeStringToFile(new File("E:\\changejson\\1-response.json"), JSON.toJSONString(jo),"UTF-8");
		FileUtils.writeStringToFile(new File("E:\\changejson\\2-response.json"), JSON.toJSONString(jo2),"UTF-8");
	}
}
