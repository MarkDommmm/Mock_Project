package project_final.service;



import org.springframework.data.domain.Page;
import project_final.entity.Category;

import java.util.List;

public interface ICategoryService extends IGenericService<Category,Category,Long>{
    Page<Category> findAll(String name, int page, int size);

    List<Category> findAll();

}
