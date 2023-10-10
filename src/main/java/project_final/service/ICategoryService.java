package project_final.model.service.impl.category;



import org.springframework.data.domain.Page;
import project_final.model.entity.Category;
import project_final.model.service.impl.IGenericService;

import java.util.List;

public interface ICategoryService extends IGenericService<Category,Category,Long>{
    Page<Category> findAll(String name, int page, int size);

    List<Category> findAll();

}
