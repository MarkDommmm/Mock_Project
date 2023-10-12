package project_final.service.mapper;

import project_final.entity.TableMenu;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;

public interface ITableMenuMapper extends IGenericMapper<TableMenu,TableMenuRequest, TableMenuCartResponse>{
TableMenuResponse toRepon(TableMenu tableMenu);
}
