package project_final.model.service.impl.table;

import org.springframework.stereotype.Component;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.model.service.impl.IGenericService;

public interface ITableService extends IGenericService<TableRequest, TableResponse,Long> {
}
