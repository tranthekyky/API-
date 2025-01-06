package com.springbootapi.service.user_impl;

import com.springbootapi.enums.RoleEnum;
import com.springbootapi.model.Role;
import com.springbootapi.model.User;
import com.springbootapi.repository.IRoleRepository;
import com.springbootapi.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private  final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository iRoleRepository;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('CREATE_POST')")
    @Override
    public List<User> findAll() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Lấy ra toàn bộ thông tin của rq đang truy cập endpoint này .
        log.info("username : {}", authentication.getName());
        log.info("role : {}", authentication.getAuthorities());
        return userRepository.findAll();
    }

    public User getInfoUser () {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return  userRepository.findByUsername(name);
    }


    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        Role role = iRoleRepository.findById(RoleEnum.USER.name())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public void update(User user, Integer id) {

    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public User findById(Integer id) {
        log.info("In Method findById");
        return userRepository.findById(id).orElse(null);
    }
}
