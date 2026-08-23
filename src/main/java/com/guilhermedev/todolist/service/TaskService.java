package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskResponseDTO;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import com.guilhermedev.todolist.exception.task.TaskNotFoundException;
import com.guilhermedev.todolist.exception.task.TaskUnauthorizedException;
import com.guilhermedev.todolist.model.Category;
import com.guilhermedev.todolist.model.Task;
import com.guilhermedev.todolist.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, CategoryService categoryService, UserService userService) {
        this.taskRepository = taskRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, Long userId) {
        log.info("Creating a new task with title: {} for user: {}", requestDTO.getTitle(), userId);

        Task entity = parseObject(requestDTO, Task.class);
        entity.setUser(userService.findById(userId));
        entity.setCategory(resolveCategory(requestDTO.getCategoryId(), userId));

        return toResponseDTO(taskRepository.save(entity));
    }

    public Page<TaskResponseDTO> getAllTasks(Long userId, int page, int size) {
        log.info("Fetching paginated tasks for user with id: {}", userId);
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByUserId(userId, pageable)
                .map(this::toResponseDTO);
    }

    public List<TaskResponseDTO> getTasksByStatus(Long userId, Status status) {
        log.info("Fetching tasks with status: {} for user with id: {}", status, userId);
        return taskRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<TaskResponseDTO> getTasksByPriority(Long userId, Priority priority) {
        log.info("Fetching tasks with priority: {} for user with id: {}", priority, userId);
        return taskRepository.findByUserIdAndPriority(userId, priority).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id, Long userId) {
        log.info("Fetching task with id: {} for user with id: {}", id, userId);
        Task task = findTaskByIdAndUserId(id, userId);
        return toResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO, Long userId) {
        log.info("Updating task with id: {} for user: {}", id, userId);
        Task existingTask = findTaskByIdAndUserId(id, userId);

        applyUpdates(existingTask, requestDTO, userId);

        return toResponseDTO(taskRepository.save(existingTask));
    }

    @Transactional
    public void deleteTask(Long id, Long userId) {
        log.info("Deleting task with id: {} for user: {}", id, userId);
        Task existingTask = findTaskByIdAndUserId(id, userId);
        taskRepository.delete(existingTask);
    }

    @Transactional
    public TaskResponseDTO markAsCompleted(Long id, Long userId) {
        log.info("Marking task with id: {} as completed for user: {}", id, userId);
        Task existingTask = findTaskByIdAndUserId(id, userId);

        existingTask.setStatus(Status.CONCLUIDA);
        return toResponseDTO(taskRepository.save(existingTask));
    }

    public Task findTaskByIdAndUserId(Long id, Long userId) {
        Task task = findTask(id);
        checkOwnership(task, userId);
        return task;
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    private void checkOwnership(Task task, Long userId) {
        if (!task.getUser().getId().equals(userId)) {
            throw new TaskUnauthorizedException(task.getId(), userId);
        }
    }

    private Category resolveCategory(Long categoryId, Long userId) {
        if (categoryId == null) {
            return null;
        }
        return categoryService.findCategoryByIdAndUserId(categoryId, userId);
    }

    private void applyUpdates(Task task, TaskRequestDTO requestDTO, Long userId) {
        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setPriority(requestDTO.getPriority());
        task.setStatus(requestDTO.getStatus());
        task.setDueDate(requestDTO.getDueDate());
        task.setCategory(resolveCategory(requestDTO.getCategoryId(), userId));
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        return parseObject(task, TaskResponseDTO.class);
    }
}