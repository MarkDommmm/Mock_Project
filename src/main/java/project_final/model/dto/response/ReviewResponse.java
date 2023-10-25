package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Reservation;
import project_final.entity.User;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private String image;
    private String comment;
    private int rating;
    private Date createdDate;
    private Reservation reservation;
    private boolean status;
}
