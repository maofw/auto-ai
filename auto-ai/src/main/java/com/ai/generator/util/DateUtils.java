package com.ai.generator.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf_10 = new SimpleDateFormat("yyyy-MM-dd");
    public static final String today19(){
        return sdf.format(new Date());
    }
    public static final String today10(){
        return sdf_10.format(new Date());
    }

    public static final String formatDateFull(Date date){
        return sdf.format(date);
    }
    public static final Date parseDateFull(String str){
        if(str == null || str.length()<=0){
            return null;
        }
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Integer age(String birthday){
        if(birthday != null && !"".equals(birthday)){
            try {
                Date date = sdf_10.parse(birthday);
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                calendar.setTime(date);
                int dateYear = calendar.get(Calendar.YEAR);
                return currentYear - dateYear ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null ;
    }
    /**
     * 获取host
     * @param url
     * @return
     */
    public static String getRefer(String url){
        //判断是否http url地址
        String murl = url == null ? null : url.toLowerCase();
        if (murl != null) {
            int idx = -1;
            if(murl.startsWith("http://")){
                idx = 7 ;
            } else if(murl.startsWith("https://")){
                idx = 8 ;
            }
            if(idx>=0){
                int endIdx = murl.indexOf("/",idx) ;
                if(endIdx>=0){
                    murl = murl.substring(0,endIdx);
                }
            }
            return murl;
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(getRefer("https://www.baidu.com/adf/dfdeadf"));
        System.out.println(getRefer("http://www.baidu.com/adf/dfdeadf"));
        System.out.println(getRefer("https://www.baidu.com"));
        System.out.println(getRefer("http://www.baidu.com/"));
        System.out.println(getRefer("https://www.baidu.com/"));
        System.out.println(getRefer("http://www.baidu.com"));
    }
}
