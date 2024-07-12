package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.template.core.entity.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentId(String paymentId);

    Optional<List<Payment>> findAllByOwnerPhone(Long phone);

    Optional<List<Payment>> findAllByRecipientPhone(Long phone);

    Optional<List<Payment>> findAllByOwnerPhoneAndStatus(Long phone, int status);

    Optional<List<Payment>> findAllByRecipientPhoneAndStatus(Long phone, int status);
}
