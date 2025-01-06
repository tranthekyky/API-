package com.springbootapi.config;

import com.springbootapi.dto.request.IntrospectRequest;
import com.springbootapi.service.user_impl.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component

public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    private AuthenticateService authenticateService;
    private  NimbusJwtDecoder nimbusJwtDecoder ;

    @Value("${signer-key}")
    private String SIGNER_KEY;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            authenticateService.introspectToken(IntrospectRequest.builder()
                            .token(token)
                            .build());
        }catch (Exception e){
            throw new JwtException(e.getMessage());
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        NimbusJwtDecoder
        .withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build() ;

        return nimbusJwtDecoder.decode(token);
    }
}
