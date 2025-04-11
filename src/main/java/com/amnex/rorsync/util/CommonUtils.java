package com.amnex.rorsync.util;

import com.amnex.rorsync.configuration.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommonUtils {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public String getUserId(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader("Authorization");
        String userId = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
                return userId;
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
                return userId;
            } catch (Exception e) {
                logger.info("JWT Token has expired" + e);
                return userId;
            }
        } else {
             logger.warn("JWT Token does not begin with Bearer String");
        }
        return userId;
    }
}
