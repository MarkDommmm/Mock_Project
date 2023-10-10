package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.entity.Category;
import project_final.model.dto.request.CategoryRequest;
import project_final.model.dto.response.CategoryResponse;
import project_final.service.IUploadService;
@Component
@AllArgsConstructor
public class CategoryMapper implements ICategoryMapper{
    private final IUploadService uploadService;
    @Override
    public Category toEntity(CategoryRequest categoryRequest) {
        String image = uploadService.uploadFile(categoryRequest.getImage());
        return Category.builder().id(categoryRequest.getId())
                .name(categoryRequest.getName())
                .image(image)
                .description(categoryRequest.getDescription())
                .status(true).build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .description(category.getDescription())
                .status(category.isStatus()).build();
    }
}
