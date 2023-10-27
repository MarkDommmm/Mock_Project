package project_final.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project_final.entity.Review;
import project_final.entity.User;

public interface IReviewService <K,V,E>{
    Page<V> findAll(int page,int size);
    Page<V> findAllByStatus(int page, int size);
    Page<V> findAllByUser(E id,int page, int size);
    V findById(E id);
    String save(K k, Long id);
    void delete(E id,User user);
    void changeStatus(E id);

}
