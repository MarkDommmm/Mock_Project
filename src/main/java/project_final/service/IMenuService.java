package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.Menu;

import java.util.List;

public interface IMenuService<K,V,E> {
   List<V> getAll(String name);
   Page<V> findAll(String name,int page,int size);
   List<V> findTopSellingMenus();
   Page<V> findAllByCategoryName(String category,int page,int size);
   V findById(E id);
   void save(K k);
   void delete(E id);
}
