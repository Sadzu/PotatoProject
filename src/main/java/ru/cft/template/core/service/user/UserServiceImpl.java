package ru.cft.template.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.user.UserCreateRequest;
import ru.cft.template.api.dto.user.UserDto;
import ru.cft.template.api.dto.user.UserPatchRequest;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ErrorCode;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.LocalDateTime;

import static ru.cft.template.core.Messages.USER_NOT_FOUND;
import static ru.cft.template.core.exception.ErrorCodes.INVALID_REQUEST;
import static ru.cft.template.core.exception.ErrorCodes.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    @Override
    public UserDto getById(String id) {
        return userMapper.map(getByIdOrThrow(id));
    }

    @Override
    public UserDto create(UserCreateRequest userCreateRequest) {
        User user = buildNewUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Wallet wallet = Wallet.builder()
                .balance(100L)
                .user(user)
                .walletId(user.getPhone() - 12345678007L)
                .build();

        user.setWalletId(wallet.getWalletId());

        userRepository.save(user);
        walletRepository.save(wallet);

        return userMapper.map(user);
    }

    private User buildNewUser(UserCreateRequest dto) {
        User newUser = User.builder()
                .lastName(dto.lastName())
                .firstName(dto.firstName())
                .email(dto.patronymicName())
                .birthday(dto.birthday())
                .phone(dto.phone())
                .email(dto.email())
                .enabled(true)
                .password(dto.password())
                .build();
        if (dto.patronymicName() != null) {
            newUser.setPatronymicName(dto.patronymicName());
        }

        return newUser;
    }

    @Override
    public UserDto update(UserPatchRequest userPatchRequest) {
        if (userPatchRequest == null) {
            throw new ServiceException(new ErrorCode(INVALID_REQUEST), "Empty patch request");
        }
        User user = Context.get().getUser();
        userMapper.map(user, userPatchRequest);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return userMapper.map(user);
    }

    private User getByIdOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(new ErrorCode(OBJECT_NOT_FOUND), String.format(USER_NOT_FOUND, id)));
    }
}
