package com.nyist.interceptor;

import com.nyist.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            request.setAttribute("error","请先登录呦~");
            request.getRequestDispatcher("/loginUI").forward(request,response);
            return  false;
        }
        else {
            return true;
        }
    }
}
