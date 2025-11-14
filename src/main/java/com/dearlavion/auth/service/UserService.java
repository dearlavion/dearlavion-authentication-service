package com.dearlavion.auth.service;

import com.dearlavion.auth.model.User;
import com.dearlavion.auth.model.UserVO;
import com.dearlavion.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    public User registerUser(UserVO userVO) {
        User user = User.builder()
                .username(userVO.getUsername())
                .email(userVO.getEmail())
                .phone(userVO.getPhone())
                .password(passwordEncoder.encode(userVO.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User updateUser(String username, UserVO u) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (u.getFirstname() != null) user.setFirstname(u.getFirstname());
        if (u.getLastname() != null) user.setLastname(u.getLastname());
        if (u.getPhone() != null) user.setPhone(u.getPhone());
        //if (u.getImage() != null) user.setImage(u.getImage());

        return userRepository.save(user); // <-- this updates existing row
    }
}
