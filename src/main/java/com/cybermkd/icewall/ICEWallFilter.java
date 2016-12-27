package com.cybermkd.icewall;

import com.cybermkd.icewall.request.ICEWallRequestWrapper;
import com.cybermkd.icewall.request.ICEWallResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * Waf防火墙过滤器
 */
public class ICEWallFilter implements Filter {

    private static final Logger logger = Logger.getLogger("WafFilter");

    private static String OVER_URL = null;//非过滤地址

    private static boolean FILTER_XSS = true;//开启XSS脚本过滤

    private static boolean FILTER_SQL = true;//开启SQL注入过滤

    private static boolean FILTER_MONGO = true;//开启MONGO注入过滤

    private static boolean FILTER_GZIP = false;//开启Gzip压缩

    public void init(FilterConfig config) throws ServletException {
        //读取Web.xml配置地址
        OVER_URL = config.getInitParameter("whitelists");

        FILTER_XSS = getParamConfig(config.getInitParameter("xss"));
        FILTER_SQL = getParamConfig(config.getInitParameter("sql"));
        FILTER_MONGO = getParamConfig(config.getInitParameter("mongo"));
        FILTER_GZIP = getParamConfig(config.getInitParameter("gzip"));
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // HttpServletResponse res = (HttpServletResponse) response;

        boolean isOver = inContainURL(req, OVER_URL);

        /** 非拦截URL、直接通过. */
        if (!isOver) {
//            try {
                //Request请求XSS过滤
                chain.doFilter(new ICEWallRequestWrapper(req, FILTER_XSS, FILTER_SQL,FILTER_MONGO), response);

                HttpServletResponse resp = (HttpServletResponse) response;

                ICEWallResponseWrapper iceResponse = new ICEWallResponseWrapper(resp);

                if (FILTER_GZIP){

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    GZIPOutputStream gzipout = new GZIPOutputStream(baos);

                    byte[] b = iceResponse.getOldBytes();

                    gzipout.write(b);

                    gzipout.close();

                    b = baos.toByteArray();

                    resp.setHeader("content-encoding", "gzip");

                    resp.setContentLength(b.length);

                    resp.getOutputStream().write(b);
                }

//            } catch (Exception e) {
//                logger.severe(" ICEWall exception , requestURL: " + req.getRequestURL());
//            }
            return;
        }

        chain.doFilter(request, response);



    }


    public void destroy() {
        logger.warning(" ICEWall destroy .");
    }


    /**
     * @param value 配置参数
     * @return 未配置返回 True
     * @Description 获取参数配置
     */
    private boolean getParamConfig(String value) {
        if (value == null || "".equals(value.trim())) {
            //未配置默认 True
            return true;
        }
        return new Boolean(value);
    }


    /**
     * <p>
     * getRequestURL是否包含在URL之内
     * </p>
     *
     * @param request
     * @param url     参数为以';'分割的URL字符串
     * @return
     */
    public static boolean inContainURL(HttpServletRequest request, String url) {
        boolean result = false;
        if (url != null && !"".equals(url.trim())) {
            String[] urlArr = url.split(";");
            StringBuffer reqUrl = new StringBuffer(request.getRequestURL());
            for (int i = 0; i < urlArr.length; i++) {
                if (reqUrl.indexOf(urlArr[i]) > 1) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
