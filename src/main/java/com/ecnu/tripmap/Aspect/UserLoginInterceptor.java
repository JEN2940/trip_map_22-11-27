package com.ecnu.tripmap.Aspect;

import com.ecnu.tripmap.mysql.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null)
                return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }


}
