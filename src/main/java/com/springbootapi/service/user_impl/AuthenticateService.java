package com.springbootapi.service.user_impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springbootapi.dto.request.AuthenticateRequest;
import com.springbootapi.dto.request.IntrospectRequest;
import com.springbootapi.dto.response.AuthenticateResponse;
import com.springbootapi.dto.response.IntrospectResponse;
import com.springbootapi.model.User;
import com.springbootapi.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
@Slf4j
public class AuthenticateService {

    @NonFinal
    @Value("${signer-key}")
    String SIGNER_KEY;

    IUserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public AuthenticateResponse authenticate (AuthenticateRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new EntityNotFoundException ("User not existed with username " + request.getUsername());
        }
        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());

        // Return token
        if (result) {
            var token = generateToken(user) ;
            return AuthenticateResponse.builder()
                    .isAuthenticated(true)
                    .token(token)
                    .build();
        }else {
            throw new EntityNotFoundException("Password does not match !");
        }
    }

    public String generateToken(User user) {
        // Để generate một token phải cần 1 header - payload - signerKey
        JWSHeader jweHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("Spring Boot")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope" , buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()) ;
        JWSObject jwsObject = new JWSObject(jweHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("JWT serialization failed " , e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspectToken(IntrospectRequest request) {
        var token = request.getToken();
        //Lấy về token đầu vào
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            // Tạo một đối tượng xác minh token với agr HMAC (Hash based message authentication code) và code key của server
            SignedJWT signedJWT = SignedJWT.parse(token) ;
            //Phân tích token thành một signedKey ( có Đầy đủ cấu trúc của một token)
            Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
            // getJWTClaimsSet lấy tập hợp các thông tinh từ payload , getExpirationTime lấy thời điểm hết hạn
            var verified = signedJWT.verify(verifier);
            //Kiểm tra tính hợp lệ của key so với SIGNER_KEY .
            return IntrospectResponse.builder()
                    .isValid(verified && expirationDate.after(new Date()))
                    .build();

        } catch (JOSEException  | ParseException e) {
            log.error("JWT verification failed " , e);
            throw new RuntimeException("JWT verification failed !", e);
        }
    }

    public String buildScope (User user) {
        StringJoiner scopes = new StringJoiner(" ");
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            scopes.add(user.getRoles()) ;
        }

        return scopes.toString();
    }
}
