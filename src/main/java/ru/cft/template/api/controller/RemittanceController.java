package ru.cft.template.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.api.dto.remittance.RemittanceCreateRequest;
import ru.cft.template.api.dto.remittance.RemittanceDto;
import ru.cft.template.core.service.remittance.RemittanceService;

import java.util.List;

import static ru.cft.template.api.Paths.*;

@Tag(name = "Переводы")
@Validated
@RestController
@RequiredArgsConstructor
public class RemittanceController {

    private final RemittanceService remittanceService;

    @Operation(description = """
            Создание перевода
            """)
    @PostMapping(REMITTANCE_CREATE)
    public RemittanceDto create(@Valid @RequestBody RemittanceCreateRequest remittanceCreateRequest) {
        return remittanceService.create(remittanceCreateRequest);
    }

    @Operation(description = """
            Получение информации о переводе по ID
            """)
    @GetMapping(REMITTANCE_INFO)
    public RemittanceDto getRemittanceInfo(@PathVariable Long id) {
        return remittanceService.getById(id);
    }

    @Operation(description = """
            Получение истории переводов, где пользователь является отправителем
            """)
    @GetMapping(REMITTANCES_HISTORY_OWNER)
    public List<RemittanceDto> getRemittanceHistoryOwner() {
        return remittanceService.getHistoryOwner();
    }

    @Operation(description = """
            Получение истории переводов, где пользователь является получателем
            """)
    @GetMapping(REMITTANCES_HISTORY_RECIPIENT)
    public List<RemittanceDto> getRemittanceHistoryRecipient() {
        return remittanceService.getHistoryRecipient();
    }
}
