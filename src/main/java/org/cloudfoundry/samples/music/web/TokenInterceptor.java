package org.cloudfoundry.samples.music.web;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jian S on 27/03/2017.
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        System.out.println("---Before Method Execution--- User:      " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        System.out.println("---Before Method Execution--- Details:   " + SecurityContextHolder.getContext().getAuthentication().getDetails());
        return true;
    }
    @Override
    public void postHandle(	HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("---After Method Execution---");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        System.out.println("---Request Completed---");
    }
}