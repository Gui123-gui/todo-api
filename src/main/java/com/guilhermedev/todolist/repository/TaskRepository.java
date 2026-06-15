package com.guilhermedev.todolist.repository;

import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import com.guilhermedev.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findByUserIdAndStatus(Long userId, Status status);
    List<Task> findByUserIdAndPriority(Long userId, Priority priority);
}
