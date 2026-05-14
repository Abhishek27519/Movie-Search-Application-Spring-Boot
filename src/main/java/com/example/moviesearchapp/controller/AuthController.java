package com.example.moviesearchapp.controller;

import com.example.moviesearchapp.security.JwtUtil;
import com.example.moviesearchapp.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> createAuthenticationToken(@RequestBody Map<String, String> authRequest) throws Exception {
        try {
            // Validate credentials against H2 DB
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.get("username"), authRequest.get("password"))
            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        // Generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.get("username"));
        final String jwt = jwtUtil.generateToken(userDetails);

        return Map.of("jwt", jwt);
    }
}