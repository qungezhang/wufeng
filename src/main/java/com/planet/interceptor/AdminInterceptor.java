package com.planet.interceptor;

import com.planet.admin.domain.Admin;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author aiveily
 * @ClassName:
 * @Description:
 * @date 2018/9/5
 */
public class AdminInterceptor implements HandlerInterceptor {

    private static final String[] IGNORE_URI = {"/admin/login"};

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("Admin-startTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求的路径
        // 得到请求的url
        String url = request.getRequestURI();

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
