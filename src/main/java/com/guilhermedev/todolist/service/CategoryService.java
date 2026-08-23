package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.dto.category.CategoryRequestDTO;
import com.guilhermedev.todolist.dto.category.CategoryResponseDTO;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.exception.category.CategoryNotFoundException;
import com.guilhermedev.todolist.exception.category.CategoryUnauthorizedException;
import com.guilhermedev.todolist.model.Category;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public List<CategoryResponseDTO> getAllCategories(Long userId) {
        log.info("Fetching all categories for user with id: {}", userId);
        return categoryRepository.findByUserId(userId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO category, Long userId) {
        log.info("Creating a new category with name: {}", category.getName());
        var entity = parseObject(category, Category.class);

        User user = userService.findById(userId);
        entity.setUser(user);

        return toResponseDTO(categoryRepository.save(entity));
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category, Long userId) {
        log.info("Updating category with id: {}", id);
        Category c = findCategory(id);
        checkOwnership(c, userId);

        c.setName(category.getName());
        c.setColor(category.getColor());
        return toResponseDTO(categoryRepository.save(c));
    }

    public void deleteCategory(Long id, Long userId) {
        log.info("Deleting category with id: {}", id);
        Category c = findCategory(id);
        checkOwnership(c, userId);

        categoryRepository.delete(c);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private void checkOwnership(Category category, Long userId) {
        if (!category.getUser().getId().equals(userId)) {
            throw new CategoryUnauthorizedException(category.getId(), userId);
        }
    }

    public Category findCategoryByIdAndUserId(Long categoryId, Long userId) {
        log.info("Fetching category with id: {} for user with id: {}", categoryId, userId);
        Category category = findCategory(categoryId);
        checkOwnership(category, userId);
        return category;
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO dto = parseObject(category, CategoryResponseDTO.class);
        dto.setUserId(category.getUser().getId());
        return dto;
    }
}