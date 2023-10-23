package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.Menu;

import java.util.List;
import java.util.Map;

public interface IMenuService<K,V,E> {
   List<V> getAll(String name);
   Page<V> findAll(String name,int page,int size);
   Page<V> findAllByStatusIsTrueAndName(String name,int page,int size);
   List<V> findTopSellingMenus();
   List<Map<String, Object>> findTopMenusByMonth();
   Page<V> findAllByCategoryName(String category,int page,int size);
   V findById(E id);
   void save(K k);
   void delete(E id);
   void changeStatus(E id);
}
