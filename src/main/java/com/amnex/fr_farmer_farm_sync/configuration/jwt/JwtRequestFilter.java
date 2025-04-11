package com.amnex.fr_farmer_farm_sync.configuration.jwt;

import com.amnex.fr_farmer_farm_sync.util.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Value("${app.excludeURLs}")
    List<String> excludeURLs;

    @Value("${allow.multipleSessions}")
    private Boolean multipleSessions;

    private final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        final String requestFixedTokenHeader = request.getHeader("external-api-token");
        String apiUrl = request.getRequestURI().toString();
        Boolean isURLExcluded = checkExcludeURLs(apiUrl);
        if (!isURLExcluded) {
            String username = getUserName(requestTokenHeader);
            // Once we get the token validate it.

            //validate external api call using encrypted token
            if (StringUtils.isNotBlank(requestFixedTokenHeader)) {
                String decodedRequestTokenHeader = new String(Base64.getDecoder().decode(requestFixedTokenHeader));
                if (StringUtils.isNotBlank(decodedRequestTokenHeader)) {
                    if (Constants.EXTERNAL_API_KEY.equals(decodedRequestTokenHeader)) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.setHeader("Content-Type", "application/json");
                        response.setStatus(401);
                        return;
                    }
                } else {
                    response.setHeader("Content-Type", "application/json");
                    response.setStatus(401);
                    return;
                }
            } else if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String jwtToken = requestTokenHeader.substring(7);
                try {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    if ((multipleSessions.equals(true) || jwtTokenUtil.validateToken(jwtToken, userDetails))
                            && userDetails.getPassword() != null
                            && userDetails.getPassword().equals(jwtToken)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        filterChain.doFilter(request, response);
                    } else {
                        response.setHeader("Content-Type", "application/json");
                        response.setStatus(401);
                        return;
                    }
                } catch (UsernameNotFoundException u) {
                    response.setHeader("Content-Type", "application/json");
                    response.setStatus(401);
                    return;
                }
//				catch (ExpiredJwtException e) {
//					response.setHeader("Content-Type", "application/json");
//					response.setStatus(401);
//					return;
//				}
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                response.setHeader("Content-Type", "application/json");
                response.setStatus(401);
                return;
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    Boolean checkExcludeURLs(String apiUrl) {
        Boolean isURLExcluded = false;
        for (String url : excludeURLs) {
            if (apiUrl.contains(url)) {
                isURLExcluded = true;
            }
        }
        return isURLExcluded;
    }

    String getUserName(String requestTokenHeader) {
        String userName = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
            } catch (Exception e) {
                logger.info("JWT Token has expired" + e);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return userName;
    }
}
