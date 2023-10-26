package project_final.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Category;
import project_final.model.dto.request.CategoryRequest;
import project_final.model.dto.response.CategoryResponse;
import project_final.repository.ICategoryRepository;
import project_final.service.ICategoryService;
import project_final.service.mapper.ICategoryMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponse> findAll(String name, int page, int size) {
        Page<Category> categories = categoryRepository.findAllByNameContains(name, PageRequest.of(page, size));
        return categories.map(categoryMapper::toResponse);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByStatusIsTrue().stream().map(categoryMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void changeStatus(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            category.get().setStatus(!category.get().isStatus());
            categoryRepository.save(category.get());
        }
    }


    @Override
    public CategoryResponse findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return categoryMapper.toResponse(category.get());
        }
        return null;
    }

    @Override
    public void save(CategoryRequest categoryRequest) {
        categoryRepository.save(categoryMapper.toEntity(categoryRequest));
    }

    @Override
    public void delete(Long id) {
        if (findById(id) != null) {
            categoryRepository.deleteById(id);
        }
    }

}
