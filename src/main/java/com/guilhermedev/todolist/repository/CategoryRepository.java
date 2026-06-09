package com.guilhermedev.todolist.repository;

import com.guilhermedev.todolist.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
