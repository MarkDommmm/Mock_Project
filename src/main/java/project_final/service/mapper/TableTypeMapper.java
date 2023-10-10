package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.entity.TableType;
import project_final.service.IUploadService;
import project_final.service.mapper.ITableTypeMapper;

@Component
@AllArgsConstructor
public class TableTypeMapper implements ITableTypeMapper {
    private final IUploadService uploadService;
    @Override
    public TableType toEntity(TableTypeRequest tableTypeRequest) {
        String image = uploadService.uploadFile(tableTypeRequest.getImage());
        return TableType.builder()
                .id(tableTypeRequest.getId())
                .name(tableTypeRequest.getName())
                .image(image)
                .description(tableTypeRequest.getDescription())
                .status(true).build();
    }

    @Override
    public TableTypeResponse toResponse(TableType tableType) {
        return TableTypeResponse.builder()
                .id(tableType.getId())
                .name(tableType.getName())
                .image(tableType.getImage())
                .description(tableType.getDescription())
                .status(tableType.isStatus()).build();
    }
}
