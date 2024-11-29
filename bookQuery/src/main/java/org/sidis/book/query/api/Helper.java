package org.sidis.book.query.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class Helper {


    @Autowired
    private JwtDecoder jwtDecoder;

    public UUID getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String newToken = token.replace("Bearer ", "");
        Jwt dToken = this.jwtDecoder.decode(newToken);
        String s = (String) dToken.getClaims().get("sub");

        return UUID.fromString(s.split(",")[0]);
    }

}
