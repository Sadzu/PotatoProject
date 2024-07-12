package ru.cft.template.core.service.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.user.UserCreateRequest;
import ru.cft.template.api.dto.user.UserDto;
import ru.cft.template.api.dto.user.UserPatchRequest;

@Service
public interface UserService {

    UserDto getById(String id);

    @Transactional
    UserDto create(UserCreateRequest userCreateRequest);

    @Transactional
    UserDto update(UserPatchRequest userPatchRequest);
}
