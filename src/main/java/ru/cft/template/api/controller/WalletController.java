package ru.cft.template.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.wallet.WalletDto;
import ru.cft.template.core.service.wallet.WalletService;

import static ru.cft.template.api.Paths.USERS_WALLETS;

@Tag(name = "Кошельки")
@Validated
@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @Operation(description = """
            Получение данных о кошельке
            """)
    @GetMapping(USERS_WALLETS)
    public WalletDto getInfo() {
        return walletService.getInfo();
    }
}
