package project_final.service.mapper;


import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.entity.Menu;
import project_final.service.mapper.IGenericMapper;

public interface IMenuMapper extends IGenericMapper<Menu, MenuRequest, MenuResponse> {
}
