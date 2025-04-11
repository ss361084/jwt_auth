package com.amnex.rorsync.configuration.jwt;

import com.amnex.rorsync.entity.UserMaster;
import com.amnex.rorsync.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.token.validity}")
    public long JWT_TOKEN_VALIDITY;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserMasterRepository userMasterRepository;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    public Boolean isTokenExpired(String token) {
        Boolean isExpired = true;
        try {
            final Date expiration = getExpirationDateFromToken(token);
            isExpired = expiration.before(new Date());
        } catch(ExpiredJwtException ex) {
            ex.printStackTrace();
            isExpired = true;
        }
        return isExpired;
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // generate token for user

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());

        Optional<UserMaster> userOptional=userMasterRepository.findByUserIdAndIsDeletedAndIsActive(Long.valueOf(userDetails.getUsername()), Boolean.FALSE, Boolean.TRUE);
        List<Integer> userAuthority = new ArrayList<>();

        if(userOptional.isPresent()){
            UserMaster userMaster=userOptional.get();
            // add user authority
            userAuthority.add(userMaster.getRoleMaster().getRoleId().intValue());
        }

        claims.put("userAuthority", userAuthority);
        claims.put("userId", userDetails.getUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Claims getData(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    public Map<String, Object>  getMapFromIoJsonwebtokenClaims(Claims claims){
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for(Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey() , entry.getValue());
        }
        return expectedMap;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String requestTokenHeader = request.getHeader("Authorization");
        token = requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")
                ? requestTokenHeader.substring(7)
                : null;
        return token;
    }
}
