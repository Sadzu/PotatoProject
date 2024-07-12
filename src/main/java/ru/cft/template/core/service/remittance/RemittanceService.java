package ru.cft.template.core.service.remittance;

import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.remittance.RemittanceCreateRequest;
import ru.cft.template.api.dto.remittance.RemittanceDto;

import java.util.List;

@Service
public interface RemittanceService {

    RemittanceDto create(RemittanceCreateRequest remittanceCreateRequest);

    RemittanceDto getById(Long id);

    List<RemittanceDto> getHistoryOwner();

    List<RemittanceDto> getHistoryRecipient();
}
