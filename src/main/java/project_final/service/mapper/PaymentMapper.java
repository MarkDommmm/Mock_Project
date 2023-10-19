package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.entity.Category;
import project_final.entity.Payment;
import project_final.model.dto.request.CategoryRequest;
import project_final.model.dto.request.PaymentRequestDTO;
import project_final.model.dto.response.CategoryResponse;
import project_final.model.dto.response.PaymentResponseDTO;
import project_final.repository.ICategoryRepository;
import project_final.service.IUploadService;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PaymentMapper implements IPaymentMapper{

    @Override
    public Payment toEntity(PaymentRequestDTO paymentRequestDTO) {
        return Payment.builder()
                .id(paymentRequestDTO.getId())
                .paymentMethod(paymentRequestDTO.getPaymentMethod())
                .status(true)
                .build();
    }

    @Override
    public PaymentResponseDTO toResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.isStatus())
                .build();
    }
}
