package com.springbootapi.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomJwtDecoder jwtDecoder;

        private final String [] PUBLIC_URLS = {"/users" , "/auth/getToken" ,
                "/auth/introspect-token" ,"/auth/logout" } ;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request.requestMatchers(HttpMethod.POST , PUBLIC_URLS).permitAll()
//                    .requestMatchers(HttpMethod.GET , "/users").hasRole(RoleEnum.ADMIN.name())   //hasAuthority("ROLE_ADMIN")
                    // Nếu sử dụng hasRole thì tiền tố mặc định sẽ là ROLE_
                    // CÒn nếu sử dụng hasAuthority thì mặc định sẽ là SCOPE_ và có thể converter
                    .anyRequest().authenticated();
        });

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
            jwtConfigurer.decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                //Config Response của một request không có quyền truy cập vào một tài nguyên
        ) ;
        http.csrf(AbstractHttpConfigurer::disable) ;

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Cấp quyển đc chuyển đổi chuẩn của Jwt
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        // Chuuyển đổi value scope mặc định "SCOPE_" --> ""
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter() ;
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        // Xác thực chuyển đổi .
        return converter ;
    }
}
