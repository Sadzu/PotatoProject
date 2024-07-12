package ru.cft.template.core.service.wallet;

import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.wallet.WalletDto;

@Service
public interface WalletService {

    WalletDto getInfo();

}
