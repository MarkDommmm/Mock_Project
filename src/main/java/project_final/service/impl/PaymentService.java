package project_final.service.impl;
import org.springframework.data.domain.Page;
import project_final.model.dto.request.PaymentRequestDTO;
import project_final.model.dto.response.PaymentResponseDTO;
import project_final.service.mapper.IPaymentServce;

public class PaymentService implements IPaymentServce {
    @Override
    public Page<PaymentResponseDTO> findAll(String name, int page, int size) {
        return null;
    }

    @Override
    public PaymentResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public void save(PaymentRequestDTO paymentRequestDTO) {

    }

    @Override
    public void delete(Long id) {

    }
}
