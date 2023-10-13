package project_final.service;

import org.springframework.data.domain.Page;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;


public interface ITableMenuService extends IGenericService<TableMenuRequest, TableMenuCartResponse,Long> {
Page<TableMenuResponse> getAll(String name,int page,int size);
void addCart(Long id);
void removeCartItem(Long id);
void changeStatus(Long id);
}