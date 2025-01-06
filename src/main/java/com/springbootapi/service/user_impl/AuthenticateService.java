package com.springbootapi.service.user_impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springbootapi.dto.request.AuthenticateRequest;
import com.springbootapi.dto.request.IntrospectRequest;
import com.springbootapi.dto.request.LogoutRequest;
import com.springbootapi.dto.response.AuthenticateResponse;
import com.springbootapi.dto.response.IntrospectResponse;
import com.springbootapi.model.InvalidatedToken;
import com.springbootapi.model.User;
import com.springbootapi.repository.IUserRepository;
import com.springbootapi.repository.InvalidatedRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Time;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

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
    InvalidatedRepository invalidatedRepository;

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

    public void logout (LogoutRequest request) throws ParseException, JOSEException {
        var jwtClaimSet = verifyToken(request.getToken()).getJWTClaimsSet() ;
        String jit = jwtClaimSet.getJWTID() ;
        Date expiration = jwtClaimSet.getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiration)
                .build();

        invalidatedRepository.save(invalidatedToken);
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
                .jwtID(UUID.randomUUID().toString())
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
    private SignedJWT verifyToken (String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        // Tạo một đối tượng xác minh token với agr HMAC (Hash based message authentication code) và code key của server
        SignedJWT signedJWT = SignedJWT.parse(token) ;
        //Phân tích token thành một signedKey ( có Đầy đủ cấu trúc của một token)
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        // getJWTClaimsSet lấy tập hợp các thông tinh từ payload , getExpirationTime lấy thời điểm hết hạn
        var verified = signedJWT.verify(verifier);

        if ( !verified && expirationDate.after(new Date())) {
            throw new JOSEException("Expired JWT token");
        }
        if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new EntityNotFoundException ("JWT ID already exists ");
        return signedJWT;
    }

    public IntrospectResponse introspectToken(IntrospectRequest request) {
        var token = request.getToken();
        //Lấy về token đầu vào
        var isValid = true ;
        try {
            verifyToken(token);
        } catch (JOSEException  | ParseException e) {
            log.error("JWT verification failed " , e);
            isValid = false ;
//            throw new RuntimeException("JWT verification failed !", e);
        }
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    public String buildScope (User user) {
        StringJoiner scopes = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scopes.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        scopes.add(permission.getName());
                    });
                }
            });
        }

        return scopes.toString();
    }
}
