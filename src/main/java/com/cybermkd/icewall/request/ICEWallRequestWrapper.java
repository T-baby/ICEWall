package com.cybermkd.icewall.request;

import com.cybermkd.icewall.ICEWallHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class ICEWallRequestWrapper extends HttpServletRequestWrapper {

    private static boolean filterXSS = true;

    private static boolean filterSQL = true;

    private static boolean filterMongo = true;


    public ICEWallRequestWrapper(HttpServletRequest request, boolean filterXSS, boolean filterSQL,boolean filterMongo) {
        super(request);
        this.filterXSS = filterXSS;
        this.filterSQL = filterSQL;
        this.filterMongo = filterMongo;
    }


    public ICEWallRequestWrapper(HttpServletRequest request) {
        this(request, true, true,true);
    }


    /**
     * @param parameter 过滤参数
     * @return
     * @Description 数组参数过滤
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = filterParamString(values[i]);
        }

        return encodedValues;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getParameterMap() {
        Map<String, String[]> primary = super.getParameterMap();
        Map<String, String[]> result = new HashMap<String, String[]>(primary.size());
        for (Map.Entry<String, String[]> entry : primary.entrySet()) {
            result.put(entry.getKey(), filterEntryString(entry.getValue()));
        }
        return result;

    }

    protected String[] filterEntryString(String[] rawValue) {
        for (int i = 0; i < rawValue.length; i++) {
            rawValue[i] = filterParamString(rawValue[i]);
        }
        return rawValue;
    }

    /**
     * @param parameter 过滤参数
     * @return
     * @Description 参数过滤
     */
    @Override
    public String getParameter(String parameter) {
        return filterParamString(super.getParameter(parameter));
    }


    /**
     * @param name 过滤内容
     * @return
     * @Description 请求头过滤
     */
    @Override
    public String getHeader(String name) {
        return filterParamString(super.getHeader(name));
    }


    /**
     * @return
     * @Description Cookie内容过滤
     */
    @Override
    public Cookie[] getCookies() {
        Cookie[] existingCookies = super.getCookies();
        if (existingCookies != null) {
            for (int i = 0; i < existingCookies.length; ++i) {
                Cookie cookie = existingCookies[i];
                cookie.setValue(filterParamString(cookie.getValue()));
            }
        }
        return existingCookies;
    }

    /**
     * @param rawValue 待处理内容
     * @return
     * @Description 过滤字符串内容
     */
    protected String filterParamString(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        String tmpStr = rawValue;
        if (this.filterXSS) {
            tmpStr = ICEWallHelper.stripXSS(rawValue);
        }
        if (this.filterSQL) {
            tmpStr = ICEWallHelper.stripSqlInjection(tmpStr);
        }
        if (this.filterMongo){
            if (!this.filterXSS){
                tmpStr=ICEWallHelper.stripXSS(rawValue);
            }
            tmpStr=ICEWallHelper.stripMongo(rawValue);
        }
        return tmpStr;
    }
}
