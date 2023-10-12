package project_final.service;



import org.springframework.data.domain.Page;
import project_final.model.dto.request.CategoryRequest;
import project_final.model.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService extends IGenericService<CategoryRequest, CategoryResponse,Long>{
    Page<CategoryResponse> findAll(String name, int page, int size);

    List<CategoryResponse> findAll();

    void changeStatus(Long id);
}
