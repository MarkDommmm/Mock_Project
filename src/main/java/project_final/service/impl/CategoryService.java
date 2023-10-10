package project_final.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Category;
import project_final.repository.ICategoryRepository;
import project_final.service.ICategoryService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;


    @Override
    public Page<Category> findAll(String name, int page, int size) {
        return categoryRepository.findAllByNameContains(name, PageRequest.of(page, size));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> findAll(int page, int size) {
        return null;
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    @Override
    public void save(Category category) {
        category.setStatus(true);
        categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        if (findById(id) != null) {
            categoryRepository.deleteById(id);
        }
    }

}
