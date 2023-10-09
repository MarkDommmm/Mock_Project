package project_final.model.service.mapper;

public interface IGenericMapper<K,T,V> {
    K toEntity(T t);
    V toResponse(K k);
}
