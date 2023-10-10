package project_final.model.service.mapper.table;

import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.model.entity.Tables;
import project_final.model.service.mapper.IGenericMapper;

public interface ITableMapper extends IGenericMapper<Tables, TableRequest, TableResponse> {
}
