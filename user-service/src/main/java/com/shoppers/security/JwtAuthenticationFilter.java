package com.shoppers.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization"); //authorization
        LOGGER.info("header: {}",requestHeader); // Bearer token
        String userName = null;
        String token = null;
        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);
            try{
                userName = this.jwtHelper.getUsernameFromToken(token);
            }
            catch (IllegalArgumentException e)
            {
                LOGGER.info("Illegal Argument while fetching the user name");
                e.printStackTrace();
            }
            catch (ExpiredJwtException e){
                LOGGER.info("Given Jwt Token is expired");
                e.printStackTrace();
            }
            catch (MalformedJwtException e){
                LOGGER.info("Some change has been done in token! Invalid Token");
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            LOGGER.info("invalid header value: {}", requestHeader);
        }

        if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //fetch user detail from user name
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            Boolean validateToken = this.jwtHelper.validateToken(token,userDetails);
            if (validateToken) {
                //set authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{
                LOGGER.info("validation fails");
            }
        }
        filterChain.doFilter(request,response);
    }
}
