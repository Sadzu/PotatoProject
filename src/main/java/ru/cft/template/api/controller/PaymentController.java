package ru.cft.template.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.api.dto.payment.PaymentCreateRequest;
import ru.cft.template.api.dto.payment.PaymentDto;
import ru.cft.template.core.service.payment.PaymentService;

import java.util.List;

import static ru.cft.template.api.Paths.*;

@Tag(name = "Счета на оплату")
@Validated
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(description = """
            Создание счета на оплату
            """)
    @PostMapping(PAYMENT_CREATE)
    public PaymentDto create(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return paymentService.create(paymentCreateRequest);
    }

    @Operation(description = """
            Отмена счета на оплату
            """)
    @PostMapping(PAYMENT_CANCEL)
    public PaymentDto cancel(@PathVariable String id) {
        return paymentService.cancel(id);
    }

    @Operation(description = """
            Оплата счета на оплату
            """)
    @PostMapping(PAYMENT_PAY)
    public PaymentDto pay(@PathVariable String id) {
        return paymentService.pay(id);
    }

    @Operation(description = """
            Информация о счете на оплату
            """)
    @GetMapping(PAYMENT_INFO)
    public PaymentDto getPaymentInfo(@PathVariable String id) {
        return paymentService.getPaymentInfo(id);
    }

    @Operation(description = """
            История выставленных счетов
            """)
    @GetMapping(PAYMENTS_OWNER)
    public List<PaymentDto> getPaymentsOwner() {
        return paymentService.getPaymentsOwner();
    }

    @Operation(description = """
            История полученных счетов
            """)
    @GetMapping(PAYMENTS_RECIPIENT)
    public List<PaymentDto> getPaymentsRecipient() {
        return paymentService.getPaymentsRecipient();
    }

    @Operation(description = """
            Фильтрация истории выставленных счетов по статусу
            """)
    @GetMapping(PAYMENTS_OWNER_FILTER)
    public List<PaymentDto> getPaymentsOwnerFilter(@PathVariable int status) {
        return paymentService.getPaymentsOwnerFilter(status);
    }

    @Operation(description = """
            Фильтрация истории полученных счетов по статусу
            """)
    @GetMapping(PAYMENTS_RECIPIENT_FILTER)
    public List<PaymentDto> getPaymentsRecipientFilter(@PathVariable int status) {
        return paymentService.getPaymentsRecipientFilter(status);
    }
}
