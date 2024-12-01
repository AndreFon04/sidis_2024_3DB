package org.sidis.book.command.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class Helper {

    @Autowired
    private JwtDecoder jwtDecoder;

    public String getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String newToken = token.replace("Bearer ", "");
        Jwt dToken = this.jwtDecoder.decode(newToken);
        String s = (String) dToken.getClaims().get("sub");

        return s.split(",")[0];
    }
}
