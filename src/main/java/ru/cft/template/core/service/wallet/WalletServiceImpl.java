package ru.cft.template.core.service.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.wallet.WalletDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ErrorCode;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.WalletMapper;
import ru.cft.template.core.repository.WalletRepository;

import java.util.Optional;

import static ru.cft.template.core.exception.ErrorCodes.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    public WalletDto getInfo() {
        return walletMapper.map(getWalletOrThrow());
    }

    private Wallet getWalletOrThrow() {
        Optional<Wallet> wallet = walletRepository.findByUserId(Context.get().getUser().getId());
        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new ServiceException(new ErrorCode(OBJECT_NOT_FOUND), String.format("User's wallet not found with phone %s", Context.get().getUser().getPhone()));
        }
    }
}
