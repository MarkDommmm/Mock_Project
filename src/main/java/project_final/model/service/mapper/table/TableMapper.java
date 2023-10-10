package project_final.model.service.mapper.table;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.model.entity.Tables;
import project_final.model.service.impl.upload_file.IUploadService;

@Component
@AllArgsConstructor
public class TableMapper implements ITableMapper{
    private final IUploadService uploadService;
    @Override
    public Tables toEntity(TableRequest tableRequest) {
        String tableImage = uploadService.uploadFile(tableRequest.getTableImage());
        return Tables.builder()
                .tableNumber(tableRequest.getTableNumber())
                .tableType(tableRequest.getTableType())
                .tableImage(tableImage)
                .description(tableRequest.getDescription())
                .status(true).build();
    }

    @Override
    public TableResponse toResponse(Tables tables) {
        return TableResponse.builder()
                .id(tables.getId())
                .tableImage(tables.getTableImage())
                .tableType(tables.getTableType())
                .description(tables.getDescription())
                .status(tables.isStatus()).build();
    }
}
