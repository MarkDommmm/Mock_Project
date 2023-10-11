package project_final.service;

import org.springframework.data.domain.Page;

public interface IGenericService<T,K,E> {
    Page<K> findAll(String name,int page,int size);
    K findById(E id);
    void save(T t);
    void delete(E id);
}
