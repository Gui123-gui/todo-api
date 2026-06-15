package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.dto.category.CategoryRequestDTO;
import com.guilhermedev.todolist.dto.category.CategoryResponseDTO;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseListObjects;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.model.Category;
import com.guilhermedev.todolist.model.User;
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
    @Autowired
    private UserService userService;

    /**
     * Lista todas as categorias do usuário logado.
     * Sem filtro de userId, qualquer usuário veria categorias de todo mundo -
     * mesmo problema de segurança que já corrigimos no TaskService.
     * Precisa de um findByUserId no CategoryRepository.
     */
    public List<CategoryResponseDTO> getAllCategories(Long userId) {
        log.info("Fetching all categories for user with id: {}", userId);
        return parseListObjects(categoryRepository.findByUserId(userId), CategoryResponseDTO.class);
    }

    /**
     * Cria uma nova categoria associada ao usuário logado.
     * Recebe CategoryRequestDTO (name, color) em vez da entidade -
     * o usuário não deve poder enviar id ou user diretamente.
     * O User é resolvido manualmente, igual fizemos no TaskService.
     */
    public CategoryResponseDTO createCategory(CategoryRequestDTO category, Long userId) {
        log.info("Creating a new category with name: {}", category.getName());
        var entity = parseObject(category, Category.class);

        User user = userService.findById(userId);
        entity.setUser(user);

        return parseObject(categoryRepository.save(entity), CategoryResponseDTO.class);
    }

    /**
     * Atualiza uma categoria existente do usuário logado.
     * - id vem por parâmetro (da URL), não dentro do DTO
     * - findByIdAndUserId garante que o usuário só edita as próprias categorias
     * Precisa de um findByIdAndUserId no CategoryRepository.
     */
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category, Long userId) {
        log.info("Updating category with id: {}", id);
        Category c = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("No category found with id: " + id));
        c.setName(category.getName());
        c.setColor(category.getColor());
        return parseObject(categoryRepository.save(c), CategoryResponseDTO.class);
    }

    /**
     * Remove uma categoria do usuário logado.
     * findByIdAndUserId garante que ele só pode deletar as próprias categorias.
     */
    public void deleteCategory(Long id, Long userId) {
        log.info("Deleting category with id: {}", id);
        Category c = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("No category found with id: " + id));
        categoryRepository.delete(c);
    }
}