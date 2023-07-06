package com.naqi.invitation.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naqi.invitation.services.MySQLUserDetailsService;
import com.naqi.invitation.utility.JwtUtils;



@Component
public class SessionRequestFilter extends OncePerRequestFilter {

    public static final String HEADER_STRING = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MySQLUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                try {

                    final String authHeader = request.getHeader(HEADER_STRING);

                    String accessToken = null;

                    if (authHeader != null){
                        // Token is in the form "Bearer token". Remove Bearer word and get only the Token
                        if (authHeader.startsWith("Bearer ")) {
                          accessToken = authHeader.replace("Bearer ", "");
                        }
                    }
                  

                    // String jwt = parseJwt(request); 
                    // if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    //   String username = jwtUtils.getUserNameFromJwtToken(jwt);
              
                    //   UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                      
                    //   UsernamePasswordAuthenticationToken authentication = 
                    //       new UsernamePasswordAuthenticationToken(userDetails,
                    //                                               null,
                    //                                               userDetails.getAuthorities());
                      
                    //   authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              
                    //   SecurityContextHolder.getContext().setAuthentication(authentication);
                    // }

                    if(accessToken != null && jwtUtils.validateJwtToken(accessToken)){

                      String username = jwtUtils.getUserNameFromJwtToken(accessToken);
              
                      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                      
                      UsernamePasswordAuthenticationToken authentication = 
                          new UsernamePasswordAuthenticationToken(userDetails,
                                                                  null,
                                                                  userDetails.getAuthorities());
                      
                      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              
                      SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                  } catch (Exception e) {
                    logger.error("Cannot set user authentication: {}", e);
                  }
              
                  filterChain.doFilter(request, response);

    }

    // private String parseJwt(HttpServletRequest request) {
    //     // String jwt = jwtUtils.getJwtFromCookies(request);//for cookies implementation
    //     String jwt = jwtUtils.getHeaderBearer(request);
    //     return jwt;
    //   }
}
