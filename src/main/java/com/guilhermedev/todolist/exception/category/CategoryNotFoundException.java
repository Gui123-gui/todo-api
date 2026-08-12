package com.guilhermedev.todolist.exception.category;

import com.guilhermedev.todolist.exception.GlobalException;

public class CategoryNotFoundException extends GlobalException {

    public CategoryNotFoundException(Long categoryId) {
        super("CATEGORY_NOT_FOUND", "No category found with id: " + categoryId);
    }
}
