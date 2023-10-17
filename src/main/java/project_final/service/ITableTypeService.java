package project_final.service;



import org.springframework.data.domain.Page;
import project_final.entity.TableType;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;

import java.util.List;

public interface ITableTypeService extends IGenericService<TableTypeRequest, TableTypeResponse,Long> {
    Page<TableTypeResponse> findAll(String name, int page, int size);

    List<TableTypeResponse> findAll();

    Page<TableTypeResponse> findAllByStatusIsTrueAndName(String name, int page, int size);

    void changeStatus(Long id);
}
