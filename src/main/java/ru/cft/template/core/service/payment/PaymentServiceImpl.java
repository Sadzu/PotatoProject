package ru.cft.template.core.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.payment.PaymentCreateRequest;
import ru.cft.template.api.dto.payment.PaymentDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Payment;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ErrorCode;
import ru.cft.template.core.exception.ErrorCodes;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.PaymentMapper;
import ru.cft.template.core.repository.PaymentRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.cft.template.core.service.payment.PaymentStatus.CANCELED;
import static ru.cft.template.core.service.payment.PaymentStatus.PAYED;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public PaymentDto create(PaymentCreateRequest paymentCreateRequest) {
        Payment payment = buildNewPayment(paymentCreateRequest);
        paymentRepository.save(payment);
        return paymentMapper.map(payment);
    }

    @Override
    @Transactional
    public PaymentDto cancel(String paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentId(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    String.format("No payment found with id %s", paymentId));
        }
        Payment payment = paymentOptional.get();

        if (payment.getStatus() == PAYED) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Payment %s is already payed. Can't cancel it", paymentId));
        } else if (payment.getStatus() == CANCELED) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Payment %s is already canceled", paymentId));
        }

        if (payment.getOwnerPhone() != Context.get().getUser().getPhone()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.FORBIDDEN),
                    String.format("You can't cancel payment %s, 'cause you aren't owner. Access denied",
                            paymentId));
        }

        payment.setStatus(-1);
        paymentRepository.save(payment);

        return paymentMapper.map(payment);
    }

    @Override
    @Transactional
    public PaymentDto pay(String paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentId(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    String.format("No payment found with id %s", paymentId));
        }

        Payment payment = paymentOptional.get();

        if (payment.getStatus() == CANCELED) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Payment %s is already canceled", payment.getPaymentId()));
        } else if (payment.getStatus() == PAYED) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Payment %s is already payed", payment.getPaymentId()));
        }

        if (payment.getRecipientPhone() != Context.get().getUser().getPhone()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.FORBIDDEN),
                    String.format("You can't pay for payment %s. Access denied", payment.getPaymentId()));
        }

        Wallet recipientWallet = walletRepository.findById(
                        userRepository.findByPhone(payment.getRecipientPhone()).get().getWalletId())
                .get();
        Wallet ownerWallet = walletRepository.findById(
                        userRepository.findByPhone(payment.getOwnerPhone()).get().getWalletId())
                .get();

        if (recipientWallet.getBalance() < payment.getCost()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    "Insufficient funds to pay");
        }
        recipientWallet.setBalance(recipientWallet.getBalance() - payment.getCost());
        ownerWallet.setBalance(ownerWallet.getBalance() + payment.getCost());
        walletRepository.save(ownerWallet);
        walletRepository.save(recipientWallet);
        payment.setStatus(1);
        paymentRepository.save(payment);

        return paymentMapper.map(payment);
    }

    @Override
    public PaymentDto getPaymentInfo(String paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentId(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("No payment found with id %s", paymentId));
        } else {
            Payment payment = paymentOptional.get();
            if (Context.get().getUser().getPhone() != payment.getOwnerPhone()
                    &&
                    Context.get().getUser().getPhone() != payment.getRecipientPhone()) {
                throw new ServiceException(new ErrorCode(ErrorCodes.FORBIDDEN),
                        "Access denied");
            }
            return paymentMapper.map(paymentOptional.get());
        }
    }

    @Override
    public List<PaymentDto> getPaymentsOwner() {
        Optional<List<Payment>> payments = paymentRepository.findAllByOwnerPhone(
                Context.get().getUser().getPhone()
        );

        if (payments.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.ITERNAL_SERVER_ERROR),
                    "Something went wrong");
        }
        if (payments.get().isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    "No payments found");
        } else {
            return paymentMapper.map(payments.get());
        }
    }

    @Override
    public List<PaymentDto> getPaymentsRecipient() {
        Optional<List<Payment>> payments = paymentRepository.findAllByRecipientPhone(
                Context.get().getUser().getPhone()
        );

        if (payments.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.ITERNAL_SERVER_ERROR),
                    "Something went wrong");
        }
        if (payments.get().isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    "No payments found");
        } else {
            return paymentMapper.map(payments.get());
        }
    }

    @Override
    public List<PaymentDto> getPaymentsOwnerFilter(int status) {
        if (status > 1 || status < -1) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Invalid status %s", status));
        }
        Optional<List<Payment>> payments = paymentRepository.findAllByOwnerPhoneAndStatus(
                Context.get().getUser().getPhone(), status
        );
        if (payments.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.ITERNAL_SERVER_ERROR),
                    "Something went wrong");
        }
        if (payments.get().isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    "No payments found by this filter");
        }

        return paymentMapper.map(payments.get());
    }

    @Override
    public List<PaymentDto> getPaymentsRecipientFilter(int status) {
        if (status > 1 || status < -1) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST),
                    String.format("Invalid status %s", status));
        }
        Optional<List<Payment>> payments = paymentRepository.findAllByRecipientPhoneAndStatus(
                Context.get().getUser().getPhone(), status);
        if (payments.isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.ITERNAL_SERVER_ERROR),
                    "Something went wrong");
        }
        if (payments.get().isEmpty()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    "No payments found by this filter");
        }


        return paymentMapper.map(payments.get());
    }

    private Payment buildNewPayment(PaymentCreateRequest paymentCreateRequest) {
        Payment payment = Payment.builder()
                .ownerPhone(Context.get().getUser().getPhone())
                .cost(paymentCreateRequest.cost())
                .status(0)
                .time(LocalDateTime.now())
                .build();
        if (paymentCreateRequest.comment() != null) {
            payment.setComment(paymentCreateRequest.comment());
        } else {
            payment.setComment("No comment");
        }

        Optional<User> recipient = userRepository.findByPhone(paymentCreateRequest.recipientPhone());
        if (recipient.isPresent()) {
            payment.setRecipientPhone(recipient.get().getPhone());
        } else {
            throw new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND),
                    String.format("User with phone %s not found", paymentCreateRequest.recipientPhone()));
        }

        return payment;
    }
}
