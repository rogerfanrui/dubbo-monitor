
package com.njwd.rpc.monitor.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 各id生成器
 * @author jili
 * @version 1.0
 */
public class IdFactory {
    private static Object initLock = new Object(); // 用于初始化一个生成随机数的空间对象
    private static Random randGen = null; // 随机数对象
    private static char numbersAndLetters[] = null; // 随机数生成的字符范围
    /**
     * 获取一个20位随机流水号
     * 
     * @return
     */
    public static String getRandomSerialNumber() {
        String sequence = new SimpleDateFormat("yyMMddHHmmssSSS")
                .format(new Date());
        return sequence + RandomStringUtils.randomNumeric(5);
    }
    /**
     * 获取一个20位随机流水号
     * 
     * @return
     */
    public static String getUUIDSerialNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 读取随机数字
     * 
     * @param i
     *            <int>(取值个数)
     * @return String
     */
    public static final String randomString(int i) {

        if (i < 1)
            return null;
        if (randGen == null)
            synchronized (initLock) {
                if (randGen == null) {
                    randGen = new Random();
                    numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            .toCharArray();
                }
            }
        char ac[] = new char[i];
        for (int j = 0; j < ac.length; j++)
            ac[j] = numbersAndLetters[randGen.nextInt(71)];

        return new String(ac);
    }

    /**
     * 读取随机数字
     * 
     * @param i
     *            <int>(取值个数)
     * @return String
     */
    public static final String randomNum(int i) {
        if (i < 1)
            return null;
        if (randGen == null)
            synchronized (initLock) {
                if (randGen == null) {
                    randGen = new Random();
                    numbersAndLetters = "0123456789".toCharArray();
                }
            }
        char ac[] = new char[i];
        for (int j = 0; j < ac.length; j++)
            ac[j] = numbersAndLetters[randGen.nextInt(9)];
        return new String(ac);
    }

    /**
     * 读取日期时间及随机数组成的字符串
     * 
     * @return String
     */
    public static String getDateTimeRandomStr() {
        StringBuffer str = new StringBuffer();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        str.append(calendar.get(Calendar.YEAR));
        str.append(((String.valueOf(calendar.get(Calendar.MONTH)).length() < 2) ? "0"
                + String.valueOf(calendar.get(Calendar.MONTH))
                : String.valueOf(calendar.get(Calendar.MONTH))));
        str.append(((String.valueOf(calendar.get(Calendar.DATE)).length() < 2) ? "0"
                + String.valueOf(calendar.get(Calendar.DATE))
                : String.valueOf(calendar.get(Calendar.DATE))));
        str.append(((String.valueOf(calendar.get(Calendar.HOUR)).length() < 2) ? "0"
                + String.valueOf(calendar.get(Calendar.HOUR))
                : String.valueOf(calendar.get(Calendar.HOUR))));
        str.append(((String.valueOf(calendar.get(Calendar.MINUTE)).length() < 2) ? "0"
                + String.valueOf(calendar.get(Calendar.MINUTE))
                : String.valueOf(calendar.get(Calendar.MINUTE))));
        str.append(((String.valueOf(calendar.get(Calendar.SECOND)).length() < 2) ? "0"
                + String.valueOf(calendar.get(Calendar.SECOND))
                : String.valueOf(calendar.get(Calendar.SECOND))));
        str.append((new Random()).nextFloat());
        return str.toString();
    }

    

}
