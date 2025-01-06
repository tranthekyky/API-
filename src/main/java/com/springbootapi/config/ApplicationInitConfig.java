package com.springbootapi.config;


import com.springbootapi.enums.RoleEnum;
import com.springbootapi.model.Role;
import com.springbootapi.model.User;
import com.springbootapi.repository.IRoleRepository;
import com.springbootapi.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ApplicationInitConfig {

    IRoleRepository roleRepository;

    @Bean
    public ApplicationRunner init(IUserRepository userRepository) {
         return args -> {
             if (userRepository.findByUsername("admin") == null) {
                 Set<Role> roles = new HashSet<>();
                 roles.add(roleRepository.findById(RoleEnum.ADMIN.name())
                 .orElseThrow(() -> new RuntimeException("Admin Role not found")));
                     User user =  User.builder()
                             .username("admin")
                             .password(passwordEncoder().encode("admin"))
                             .roles(roles)
                             .build() ;
                     userRepository.save(user) ;
                 } ;
         };

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10) ;
    }
}
