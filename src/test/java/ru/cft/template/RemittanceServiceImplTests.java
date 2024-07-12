package ru.cft.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.template.api.dto.remittance.RemittanceCreateRequest;
import ru.cft.template.api.dto.remittance.RemittanceDto;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.RemittanceMapper;
import ru.cft.template.core.repository.RemittanceRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.remittance.RemittanceServiceImpl;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class RemittanceServiceImplTests {

    @Mock
    private RemittanceRepository remittanceRepository;

    @Mock
    private RemittanceMapper remittanceMapper;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RemittanceServiceImpl remittanceServiceImpl;

    @Test
    public void testCreateRemittanceByPhoneFail() {
        RemittanceCreateRequest remittanceCreateRequest = RemittanceCreateRequest.builder()
                .recipientPhone(70000000000L)
                .cost(10L)
                .build();

        RemittanceDto remittanceDto = RemittanceDto.builder()
                .remittanceId(1L)
                .recipientPhone(70000000000L)
                .cost(10L)
                .time(LocalDateTime.now())
                .build();

        Assertions.assertThrows(ServiceException.class,
                () -> remittanceServiceImpl.create(remittanceCreateRequest),
                "Не найден пользователь с указанным номером телефона: 70000000000");
    }

    @Test
    public void testCreateRemittanceByWalletFail() {
        RemittanceCreateRequest remittanceCreateRequest = RemittanceCreateRequest.builder()
                .recipientWallet(1242352456L)
                .cost(10L)
                .build();

        RemittanceDto remittanceDto = RemittanceDto.builder()
                .remittanceId(1L)
                .recipientWallet(1242352456L)
                .cost(10L)
                .time(LocalDateTime.now())
                .build();

        Assertions.assertThrows(ServiceException.class,
                () -> remittanceServiceImpl.create(remittanceCreateRequest));
    }

    @Test
    public void testGetHistoryOwnerFail() {
        User user = User.builder()
                .phone(70000000000L)
                .id(1L)
                .build();

        Assertions.assertThrows(ServiceException.class,
                () -> remittanceServiceImpl.getHistoryOwner());
    }

    @Test
    public void testGetHistoryRecipientFail() {
        User user = User.builder()
                .phone(70000000000L)
                .id(1L)
                .build();

        Assertions.assertThrows(ServiceException.class,
                () -> remittanceServiceImpl.getHistoryRecipient());
    }
}
