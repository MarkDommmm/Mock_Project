package project_final.model.service.mapper.menu;


import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.model.entity.Menu;
import project_final.model.service.mapper.IGenericMapper;

public interface IMenuMapper extends IGenericMapper<Menu, MenuRequest, MenuResponse> {
}
