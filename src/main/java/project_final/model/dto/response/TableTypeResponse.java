package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Multipart;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableTypeResponse {
    private Long id;
    private String name;
    private String image;
    private String description;
    private boolean status;
}
