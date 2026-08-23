package com.guilhermedev.todolist.repository;

import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import com.guilhermedev.todolist.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    List<Task> findByUserIdAndStatus(Long userId, Status status);
    List<Task> findByUserIdAndPriority(Long userId, Priority priority);
}