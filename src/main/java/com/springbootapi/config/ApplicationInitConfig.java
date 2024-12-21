package com.springbootapi.config;


import com.springbootapi.enums.RoleEnum;
import com.springbootapi.model.User;
import com.springbootapi.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init(IUserRepository userRepository) {
         return args -> {
             if (userRepository.findByUsername("admin") == null) {
                     User user =  User.builder()
                             .username("admin")
                             .password(passwordEncoder.encode("admin"))
                             .roles(RoleEnum.ADMIN.name())
                             .build() ;
                     userRepository.save(user) ;
                 } ;
         };

    }
}
