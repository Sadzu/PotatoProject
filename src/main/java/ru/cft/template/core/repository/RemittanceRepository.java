package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.template.core.entity.Remittance;
import ru.cft.template.core.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RemittanceRepository extends JpaRepository<Remittance, Long> {

    Optional<Remittance> findByRemittanceId(long id);

    Optional<List<Remittance>> findAllByOwner(User user);

    Optional<List<Remittance>> findAllByRecipient(User user);
}
