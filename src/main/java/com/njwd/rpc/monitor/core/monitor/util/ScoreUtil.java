package com.njwd.rpc.monitor.core.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreUtil {

	
	public static Double score(Date d,int type){
		try {
			String format = type==1?"yyyyMMddHHmm":"yyyyMMddHH";
			SimpleDateFormat sd = new SimpleDateFormat(format);
			return Double.valueOf(sd.format(d));
		} catch (Exception e) {
			e.printStackTrace();
			//nothing
			return null;
		}
	}
	
	public static String scoreDate(Date d,int type){
		try {
			String format = type==1?"yyyyMMddHHmm":"yyyyMMddHH";
			SimpleDateFormat sd = new SimpleDateFormat(format);
			return sd.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			//nothing
			return null;
		}
	}
}
