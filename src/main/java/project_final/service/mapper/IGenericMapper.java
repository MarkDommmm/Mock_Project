package project_final.service.mapper;

public interface IGenericMapper<K,T,V> {
    K toEntity(T t);
    V toResponse(K k);
}
