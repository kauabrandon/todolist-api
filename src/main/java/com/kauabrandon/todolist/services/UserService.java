package com.kauabrandon.todolist.services;

import com.kauabrandon.todolist.dtos.UserResponseDTO;
import com.kauabrandon.todolist.models.User;
import com.kauabrandon.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("Usuário não encontrado! Id: " + id + ", Tipo " + User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public UserResponseDTO findByIdAsDTO(Long id) {
        User user = findById(id);
        return new UserResponseDTO(user);
    }

    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir, há entidades relacionadas!");
        }
    }
}
