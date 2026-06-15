package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskResponseDTO;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseListObjects;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.model.Category;
import com.guilhermedev.todolist.model.Task;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.CategoryRepository;
import com.guilhermedev.todolist.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;

    /**
     * Cria uma nova task associada ao usuário logado.
     * - Converte o DTO para entidade (campos simples via Dozer)
     * - Busca e associa o User dono da task (Dozer não resolve isso)
     * - Se vier categoryId, busca e associa a Category (também manual)
     */
    public TaskResponseDTO createTask(TaskRequestDTO task, Long userId) {
        log.info("Creating a new task with title: {}", task.getTitle());
        var entity = parseObject(task, Task.class);

        // Associa o dono da task (obrigatório, vem do usuário autenticado)
        User user = userService.findById(userId);
        entity.setUser(user);

        // Categoria é opcional - só associa se o usuário enviou um categoryId
        if (task.getCategoryId() != null) {
            Category category = categoryRepository.findById(task.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            entity.setCategory(category);
        }

        return parseObject(taskRepository.save(entity), TaskResponseDTO.class);
    }

    /**
     * Lista todas as tasks pertencentes ao usuário logado.
     */
    public List<TaskResponseDTO> getAllTasks(Long userId) {
        log.info("Fetching all tasks for user with id: {}", userId);
        return parseListObjects(taskRepository.findByUserId(userId), TaskResponseDTO.class);
    }

    /**
     * Busca uma task específica, garantindo que ela pertence ao usuário logado.
     * findByIdAndUserId evita que um usuário acesse tasks de outro.
     */
    public TaskResponseDTO getTaskById(Long id, Long userId) {
        log.info("Fetching task with id: {} for user with id: {}", id, userId);
        Task idAndUser = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("No task found with id: " + id + " for user with id: " + userId));
        return parseObject(idAndUser, TaskResponseDTO.class);
    }

    /**
     * Atualiza uma task existente do usuário logado (PUT - atualização completa).
     * - Busca a task garantindo que pertence ao usuário (segurança)
     * - Atualiza todos os campos simples
     * - Trata a categoria: associa se categoryId vier preenchido,
     *   ou remove a categoria atual se vier nulo
     */
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO task, Long userId) {
        log.info("Updating task with id: {}", id);
        Task existingTask = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("No task found with id: " + id));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());

        if (task.getCategoryId() != null) {
            // Usuário enviou categoria -> busca e associa
            Category category = categoryRepository.findById(task.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingTask.setCategory(category);
        } else {
            // Usuário não enviou categoria -> remove a categoria existente (se houver)
            existingTask.setCategory(null);
        }

        return parseObject(taskRepository.save(existingTask), TaskResponseDTO.class);
    }

    /**
     * Remove uma task do usuário logado.
     * findByIdAndUserId garante que ele só pode deletar as próprias tasks.
     */
    public void deleteTask(Long id, Long userId) {
        log.info("Deleting task with id: {}", id);
        Task existingTask = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("No task found with id: " + id));
        taskRepository.delete(existingTask);
    }

}