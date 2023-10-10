package project_final.service.mapper;

import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.entity.TableType;
import project_final.service.mapper.IGenericMapper;

public interface ITableTypeMapper extends IGenericMapper<TableType,TableTypeRequest, TableTypeResponse> {
}
