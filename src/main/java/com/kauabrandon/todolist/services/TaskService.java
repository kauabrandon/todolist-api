package com.kauabrandon.todolist.services;

import com.kauabrandon.todolist.dtos.TaskResponseDTO;
import com.kauabrandon.todolist.models.Task;
import com.kauabrandon.todolist.models.User;
import com.kauabrandon.todolist.models.enums.ProfileEnum;
import com.kauabrandon.todolist.repositories.TaskRepository;
import com.kauabrandon.todolist.security.UserSpringSecurity;
import com.kauabrandon.todolist.services.exceptions.AuthorizationException;
import com.kauabrandon.todolist.services.exceptions.DataBindingViolationException;
import com.kauabrandon.todolist.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Tarefa não encontrada! Id: " + id + ", Tipo " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity,task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<TaskResponseDTO> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks.stream().map(TaskResponseDTO::new).toList();
    }

    @Transactional
    public TaskResponseDTO findByIdAsDTO(Long id) {
        Task task = findById(id);
        return new TaskResponseDTO(task);
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não foi possível excluir, há entidades relacionadas!");
        }
    }

    public boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
