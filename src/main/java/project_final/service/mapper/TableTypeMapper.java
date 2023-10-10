package project_final.model.service.mapper.tableType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.model.entity.TableType;
import project_final.model.service.impl.upload_file.IUploadService;

@Component
@AllArgsConstructor
public class TableTypeMapper implements ITableTypeMapper{
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
