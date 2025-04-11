package com.amnex.rorsync.util;

import com.amnex.rorsync.configuration.jwt.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CommonUtils {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

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

    public <T> List<T> convertJsonToList(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString,objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
