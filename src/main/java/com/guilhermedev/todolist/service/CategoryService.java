package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.model.Category;
import com.guilhermedev.todolist.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
       log.info("Fetching all categories");
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category){
        log.info("Creating a new category with name: {}", category.getName());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category){
        log.info("Updating category with id: {}", category.getId());
        Category c = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("No category found with id: " + category.getId()));;
        c.setName(category.getName());
        c.setColor(category.getColor());
        return categoryRepository.save(c);
    }

    public void deleteCategory(Long id){
        log.info("Deleting category with id: {}", id);
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No category found with id: " + id));
        categoryRepository.delete(c);

    }
}
