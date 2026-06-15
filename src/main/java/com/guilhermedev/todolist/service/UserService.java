package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Busca os dados do perfil do usuário logado.
     * Usado pelo AuthController para exibir as informações da conta.
     */
    public User getProfile(Long id) {
        log.info("Getting profile for user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
    }

    /**
     * Atualiza nome e email do usuário.
     * A senha não é alterada aqui - existe um método específico (changePassword)
     * para isso, já que envolve criptografia.
     */
    public User updateUser(long id, User user) {
        log.info("Updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        return userRepository.save(u);
    }

    /**
     * Troca a senha do usuário.
     * A nova senha é criptografada com BCrypt antes de salvar -
     * nunca armazenar senha em texto puro no banco.
     */
    public void changePassword(Long id, String newPassword) {
        log.info("Changing password for user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    /**
     * Remove a conta do usuário.
     * Como Task e Category têm @ManyToOne para User, a configuração de
     * cascade no banco (ou a ausência dela) determina o que acontece
     * com as tasks/categorias desse usuário ao deletar.
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        userRepository.delete(u);
    }

    /**
     * Busca um usuário pelo id - método de apoio usado por outros services
     * (ex: TaskService) para resolver o relacionamento User em uma Task,
     * já que o Dozer não converte userId (Long) em User automaticamente.
     */
    public User findById(Long userId) {
        log.info("Finding user with id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
    }
}