package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.User;

public interface IReviewService <T,E>{
    Page<T> findAll(int page,int size);
    T findById(E id);
    void save(T t, User user);
    void delete(E id,User user);
    void changeStatus(E id);

}
