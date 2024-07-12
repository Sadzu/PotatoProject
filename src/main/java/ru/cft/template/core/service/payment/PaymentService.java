package ru.cft.template.core.service.payment;

import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.payment.PaymentCreateRequest;
import ru.cft.template.api.dto.payment.PaymentDto;

import java.util.List;

@Service
public interface PaymentService {

    PaymentDto create(PaymentCreateRequest paymentCreateRequest);

    PaymentDto cancel(String paymentId);

    PaymentDto pay(String paymentId);

    PaymentDto getPaymentInfo(String paymentId);

    List<PaymentDto> getPaymentsOwner();

    List<PaymentDto> getPaymentsRecipient();

    List<PaymentDto> getPaymentsOwnerFilter(int status);

    List<PaymentDto> getPaymentsRecipientFilter(int status);
}
