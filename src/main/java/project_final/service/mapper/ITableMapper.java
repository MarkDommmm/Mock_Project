package project_final.service.mapper;

import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.entity.Tables;
import project_final.service.mapper.IGenericMapper;

public interface ITableMapper extends IGenericMapper<Tables, TableRequest, TableResponse> {
}
