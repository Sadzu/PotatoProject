package ru.cft.template.core.service.remittance;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.remittance.RemittanceCreateRequest;
import ru.cft.template.api.dto.remittance.RemittanceDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Remittance;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ErrorCode;
import ru.cft.template.core.exception.ErrorCodes;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.RemittanceMapper;
import ru.cft.template.core.repository.RemittanceRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemittanceServiceImpl implements RemittanceService {
    private final RemittanceRepository remittanceRepository;
    private final RemittanceMapper remittanceMapper;

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RemittanceDto create(RemittanceCreateRequest remittanceCreateRequest) {
        Remittance remittance = buildNewRemittance(remittanceCreateRequest);
        doRemittance(remittance);
        remittanceRepository.save(remittance);

        return remittanceMapper.map(remittance);
    }

    @Override
    public RemittanceDto getById(Long id) {
        return remittanceMapper.map(getByIdOrThrow(id));
    }

    @Override
    public List<RemittanceDto> getHistoryOwner() {
        return remittanceMapper.map(getByOwnerOrThrow(Context.get().getUser()));
    }

    @Override
    public List<RemittanceDto> getHistoryRecipient() {
        return remittanceMapper.map(getByRecipientOrThrow(Context.get().getUser()));
    }

    private Remittance buildNewRemittance(RemittanceCreateRequest dto) {
        if (dto.recipientPhone() == null && dto.recipientWallet() == null) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST), "No recipient phone or wallet");
        }
        Remittance newRemittance = Remittance.builder()
                .cost(dto.cost())
                .owner(Context.get().getUser())
                .build();
        if (dto.recipientPhone() != null) {
            Optional<User> user = userRepository.findByPhone(dto.recipientPhone());
            if (user.isPresent()) {
                newRemittance.setRecipient(user.get());
            } else {
                throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST), String.format(
                        "Не найден пользователь с указанным номером телефона: %s", dto.recipientPhone()
                ));
            }
        }
        if (dto.recipientWallet() != null) {
            Optional<Wallet> wallet = walletRepository.findById(dto.recipientWallet());
            if (wallet.isPresent()) {
                newRemittance.setRecipientWallet(wallet.get());
            } else {
                throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST), String.format(
                        "Не найден кошелек с указанным ID: %s", dto.recipientWallet()
                ));
            }
        }

        return newRemittance;
    }

    private void doRemittance(Remittance remittance) {
        Wallet recipientWallet;
        if (remittance.getRecipientWallet() != null) {
            recipientWallet = remittance.getRecipientWallet();
        } else if (remittance.getRecipient() != null) {
            recipientWallet = walletRepository.findById(remittance.getRecipient().getWalletId()).get();
        } else {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST));
        }
        Wallet ownerWallet = walletRepository.findById(Context.get().getUser().getWalletId()).get();
        if (ownerWallet.getBalance() < remittance.getCost()) {
            throw new ServiceException(new ErrorCode(ErrorCodes.INVALID_REQUEST), String.format(
                    "Недостаточно средств на балансе для совершения перевода. Сумма больше на %d д. е.",
                    remittance.getCost() - recipientWallet.getBalance()));
        } else {
            recipientWallet.setBalance(recipientWallet.getBalance() + remittance.getCost());
            ownerWallet.setBalance(ownerWallet.getBalance() - remittance.getCost());
            walletRepository.save(recipientWallet);
            walletRepository.save(ownerWallet);
            remittance.setTime(LocalDateTime.now());
        }
    }

    private Remittance getByIdOrThrow(Long id) {
        return remittanceRepository.findById(id)
                .orElseThrow(() -> new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND), String.format(
                        "Перевод с ID: %d не найден", id)
                ));
    }

    private List<Remittance> getByOwnerOrThrow(User user) {
        return remittanceRepository.findAllByOwner(user)
                .orElseThrow(() -> new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND), "Не найдено отправленных переводов"));
    }

    private List<Remittance> getByRecipientOrThrow(User user) {
        return remittanceRepository.findAllByRecipient(user)
                .orElseThrow(() -> new ServiceException(new ErrorCode(ErrorCodes.OBJECT_NOT_FOUND), "Не найдено полученных переводов"));
    }
}
