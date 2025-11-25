package com.backend.jwtUtils;

import com.backend.repository.userRepo;
import com.backend.services.customUserDetailsService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class jwtFilter extends OncePerRequestFilter {

    utils utils;

    userRepo repo;


    public jwtFilter(UserDetailsService userDetailsService, userRepo repo, com.backend.jwtUtils.utils utils) {
        this.userDetailsService = userDetailsService;
        this.repo = repo;
        this.utils = utils;
    }

    private final UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/Auth") ||
                request.getRequestURI().startsWith("/swagger-ui") ||
                request.getRequestURI().startsWith("/v3")) {
            filterChain.doFilter(request, response);
            return;
        }

        var header =request.getHeader("Authorization");
        if(header==null) throw new ServletException("Authorization header not found");
        String token="";

        if(header.startsWith("Bearer ")) {
             token = header.substring(7);
             if (!utils.isTokenExpired(token) ) {
                 var username="";
                 try {
                      username = utils.extractUsername(token);
                 }
                 catch (MalformedJwtException ex){
                     throw new ServletException("malformated JWT token"+ex.getMessage());
                 }

                 var user = repo.findByUsername(username);

                 if(username.equals(user.getUsername())){
                     UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                     UsernamePasswordAuthenticationToken auth =
                             new UsernamePasswordAuthenticationToken(
                                     userDetails,
                                     null,
                                     userDetails.getAuthorities()
                             );
                     SecurityContextHolder.getContext().setAuthentication(auth);
                     filterChain.doFilter(request, response);
                 }

             }else throw new RuntimeException("token expired");




        }else throw new ServletException("Invalid token");




    }
}
