package ru.cft.template.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@Entity
@Table(name = "remittances")
@NoArgsConstructor
@AllArgsConstructor
public class Remittance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remittanceId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    @ToString.Exclude
    User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_phone")
    @ToString.Exclude
    User recipient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_wallet")
    @ToString.Exclude
    Wallet recipientWallet;

    private Long cost;

    @CreationTimestamp
    LocalDateTime time;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Remittance remittance = (Remittance) o;
        return getRemittanceId() != null && Objects.equals(getRemittanceId(), remittance.getRemittanceId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
