package project_final.service.mapper;



import org.springframework.data.domain.Page;
import project_final.model.dto.request.CategoryRequest;
import project_final.model.dto.request.PaymentRequestDTO;
import project_final.model.dto.response.CategoryResponse;
import project_final.model.dto.response.PaymentResponseDTO;
import project_final.service.IGenericService;

import java.util.List;

public interface IPaymentServce extends IGenericService<PaymentRequestDTO, PaymentResponseDTO,Long> {

}
