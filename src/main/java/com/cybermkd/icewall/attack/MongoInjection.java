package com.cybermkd.icewall.attack;

import org.apache.commons.lang.StringUtils;

/**
 * Mongodb防注入
 */
public class MongoInjection implements Istrip {

    private static String[] blackList={"{", "}", "$ne", "$gte", "$gt", "$lt", "$lte", "$in", "$nin", "$exists", "$where", "tojson", "==", "db.","$where"};
    /**
     * @param value 待处理内容
     * @return
     * @Description MongoDB防注入
     */
    @Override
    public String strip(String value) {


        for (int i=0;i<blackList.length;i++){
            value=StringUtils.replace(value,blackList[i],"");
        }
        return value;
    }



}
