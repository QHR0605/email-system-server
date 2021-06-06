package com.example.server.util.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.server.config.SpringContextConfig;
import com.example.server.entity.Filter;
import com.example.server.mapper.AdminMapper;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author 全鸿润
 */
public class WebInterceptor implements HandlerInterceptor {

    private final AdminMapper adminMapper = SpringContextConfig.getBean(AdminMapper.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = IpUtil.getIp(request);
        JsonResult jsonResult = JsonResultFactory.buildJsonResult(
                JsonResultStateCode.INTERRUPTED,
                JsonResultStateCode.INTERRUPTED_DES,
                null
        );
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(jsonResult);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        System.out.println("客户端的IP地址: " + ip);
        if (ip == null || ip.isEmpty()) {
            writer = response.getWriter();
            writer.append(jsonObject.toJSONString());
            writer.flush();
            return false;
        }
        List<Filter> filters = adminMapper.selectFilter();
        for (Filter filter : filters
        ) {
            if (filter.getIpAddress().equals(ip)) {
                writer = response.getWriter();
                writer.append(jsonObject.toJSONString());
                writer.flush();
                System.out.println("拦截客户端: " + ip);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
