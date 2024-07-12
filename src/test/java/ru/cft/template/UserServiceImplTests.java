package ru.cft.template;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.service.user.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testCreateSuccess() {
        User userToSave = User.builder()
                .lastName("Тест")
                .firstName("Тест")
                .patronymicName("Тест")
                .email("test@test.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .phone(70000000001L)
                .enabled(true)
                .password("Is1tVal1dPassword?!")
                .walletId(70000000001L - 12345678007L)
                .creationDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                .updateDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                .build();

        User savedUser = User.builder()
                .lastName("Тест")
                .firstName("Тест")
                .patronymicName("Тест")
                .email("test@test.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .phone(70000000001L)
                .enabled(true)
                .password("Is1tVal1dPassword?!")
                .walletId(70000000001L - 12345678007L)
                .creationDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                .updateDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                .build();

        when(userRepository.save(userToSave)).thenReturn(savedUser);
    }
}
