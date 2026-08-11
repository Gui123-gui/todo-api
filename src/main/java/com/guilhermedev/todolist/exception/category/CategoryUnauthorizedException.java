package com.guilhermedev.todolist.exception.category;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando o usuário tenta acessar uma categoria que não pertence a ele.
 */
public class CategoryUnauthorizedException extends GlobalException {

    public CategoryUnauthorizedException(Long categoryId, Long userId) {
        super("CATEGORY_UNAUTHORIZED", "User with id: " + userId + " is not authorized to access category with id: " + categoryId);
    }
}
