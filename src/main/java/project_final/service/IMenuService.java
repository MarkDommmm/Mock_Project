package project_final.model.service.impl.menu;

import org.springframework.data.domain.Page;

public interface IMenuService<K,V,E> {
   Page<V> findAll(String name,int page,int size);
   V findById(E id);
   void save(K k);
   void delete(E id);
}
