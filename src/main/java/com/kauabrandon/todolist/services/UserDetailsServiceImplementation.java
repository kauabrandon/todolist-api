package com.kauabrandon.todolist.services;

import com.kauabrandon.todolist.models.User;
import com.kauabrandon.todolist.repositories.UserRepository;
import com.kauabrandon.todolist.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("O usuário não foi encontrado: " + username);
        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
    }
}
