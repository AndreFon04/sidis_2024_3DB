package org.sidis.authservice.auth.api;
/*
import com.example.library.readermanagement.api.ReaderView;
import com.example.library.readermanagement.api.ReaderViewMapper;
import com.example.library.readermanagement.services.CreateReaderRequest;
import com.example.library.readermanagement.services.ReaderService;
import com.example.library.usermanagement.api.UserView;
import com.example.library.usermanagement.api.UserViewMapper;
import com.example.library.usermanagement.model.User;
import com.example.library.usermanagement.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static java.util.stream.Collectors.joining;


@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/public")
public class AuthApi {

    private final AuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    private final UserViewMapper userViewMapper;

    private final UserService userService;
    private final ReaderService readerService;
    private final ReaderViewMapper readerViewMapper;


    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Valid final AuthRequest request) {
        try {


            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // if the authentication is successful, Spring will store the authenticated user
            // in its "principal"
            final User user = (User) authentication.getPrincipal();

            final Instant now = Instant.now();
            final long expiry = 36000L; // 1 hour is usually too long for a token to be valid. adjust for production

            final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(joining(" "));

            final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getId(), user.getUsername()))
                    .claim("roles", scope).build();

            final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(userViewMapper.toUserView(user));
        } catch (final BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public ReaderView register(@RequestBody @Valid final CreateReaderRequest request) {
        final var reader = readerService.create(request);
        return readerViewMapper.toReaderView(reader);
    }
}
*/
