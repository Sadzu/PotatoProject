package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import ru.cft.template.api.dto.payment.PaymentDto;
import ru.cft.template.core.entity.Payment;

import java.util.List;

@Mapper
public interface PaymentMapper {

    PaymentDto map(Payment payment);

    List<PaymentDto> map(List<Payment> payments);
}
