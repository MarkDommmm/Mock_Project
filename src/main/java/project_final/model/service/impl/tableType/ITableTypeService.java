package project_final.model.service.impl.tableType;



import org.springframework.data.domain.Page;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.model.entity.TableType;
import project_final.model.service.impl.IGenericService;

import java.util.List;

public interface ITableTypeService extends IGenericService<TableTypeRequest, TableTypeResponse,Long> {
    Page<TableTypeResponse> findAll(String name, int page, int size);

    List<TableTypeResponse> findAll();
}
