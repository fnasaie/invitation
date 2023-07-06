package com.naqi.invitation.utility;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.naqi.invitation.models.MySQLUserDetails;
import com.naqi.invitation.models.User;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${app.jwtCookieName}")
  private String jwtCookie;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  // public String getJwtFromCookies(HttpServletRequest request) {
  //   Cookie cookie = WebUtils.getCookie(request, jwtCookie);
  //   if (cookie != null) {
  //     return cookie.getValue();
  //   } else {
  //     return null;
  //   }
  // }

  // public ResponseCookie generateJwtCookie(MySQLUserDetails userPrincipal) {

  //   String jwt = getJwtToken(userPrincipal);
  //   ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path(contextPath).maxAge(24 * 60 * 60).httpOnly(true).build();
  //   return cookie;
    
  // }

  public String getHeaderBearer(HttpServletRequest request) {

    String bearerHeaderValue = request.getHeader("Authorization");

    if(bearerHeaderValue != null){
      return bearerHeaderValue;
    } else{
      return null;
    }

  }


  public String getJwtToken(MySQLUserDetails userPrincipal){
      
      String jwt = generateTokenFromUsername(userPrincipal.getUsername(),userPrincipal.getId());
      return jwt;

  }

  public String getJwtToken(User user){
      
    String jwt = generateTokenFromUsername(user.getEmail(),user.getId());
    return jwt;

  }


  public String refreshToken(String authToken){

    String refreshToken = generateTokenFromUsername(getUserNameFromJwtToken(authToken),getUserId(authToken),getIssueDate(authToken));
    return refreshToken;

  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public String getUserId(String token){
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
  }

  public Date getIssueDate(String token){
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuedAt();

  }

  public Date getExpiryDate(String token){
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();

  }

  // public ResponseCookie getCleanJwtCookie() {
  //   ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path(contextPath).build();
  //   return cookie;
  // }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  
  public String generateTokenFromUsername(String username,String jti) {   
    return Jwts.builder()
        .setSubject(username)
        .setId(jti)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String generateTokenFromUsername(String username,String jti, Date issueAt) {   
    return Jwts.builder()
        .setSubject(username)
        .setId(jti)
        .setIssuedAt(issueAt)
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }


}
