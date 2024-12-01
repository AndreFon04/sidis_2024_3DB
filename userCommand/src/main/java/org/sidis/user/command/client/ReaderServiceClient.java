package org.sidis.user.command.client;

import org.sidis.user.command.model.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class ReaderServiceClient {

//    @Value("${reader.otherinstance.url}")
//    private String userOtherInstanceUrl;

    private static final Logger log = LoggerFactory.getLogger(ReaderServiceClient.class);
    private final RestTemplate restTemplate;
    private final JwtEncoder jwtEncoder;

    public ReaderServiceClient(RestTemplate restTemplate, JwtEncoder jwtEncoder) {
        this.restTemplate = restTemplate;
        this.jwtEncoder = jwtEncoder;
    }

    public void saveReader(Reader reader) {
        try{
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject("reader-service") // Define um identificador básico para o token
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS)) // Definir uma data de expiração
                    .build();
            // Gerar o token JWT
            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            // Configurar o cabeçalho com o token JWT
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Reader> entity = new HttpEntity<>(reader, headers);

//            ResponseEntity<Reader> response = restTemplate.postForEntity(userOtherInstanceUrl + "/api/readers/internal", entity, Reader.class);

//            if (response.getStatusCode() == HttpStatus.OK) {
//                log.info("Replicação bem-sucedida para a instância secundária.");
//            } else {
//                log.warn("Replicação falhou com código de status: " + response.getStatusCode());
//            }
        } catch (Exception e) {
            log.warn("Falha ao replicar o Reader para a instância secundária. Erro: " + e.getMessage());
            // Exceção capturada para que o erro de replicação não interrompa o fluxo principal
        }
    }

//    public void saveUser(User user) {
//        try{
//            ResponseEntity<User[]> response = restTemplate.postForEntity(userOtherInstanceUrl + "/api/admin/user/internal", user, User[].class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return ;
//            } else {
//                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Instância alternativa do user-service está indisponível: " + userOtherInstanceUrl);
//            }
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//        }
//    }
}
