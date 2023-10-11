package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.Tables;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;

import java.util.List;

public interface ITableService extends IGenericService<TableRequest, TableResponse,Long> {
    List<TableResponse> getTables(String name);
}
