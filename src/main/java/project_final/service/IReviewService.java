package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.User;

public interface IReviewService <K,V,E>{
    Page<V> findAll(int page,int size);
    V findById(E id);
    void save(K k, User user);
    void delete(E id,User user);
    void changeStatus(E id);

}
