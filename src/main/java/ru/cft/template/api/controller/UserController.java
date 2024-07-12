package ru.cft.template.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.api.dto.user.UserCreateRequest;
import ru.cft.template.api.dto.user.UserDto;
import ru.cft.template.api.dto.user.UserPatchRequest;
import ru.cft.template.core.service.user.UserService;

import static ru.cft.template.api.Paths.USERS;
import static ru.cft.template.api.Paths.USER_ID;

@Tag(name = "Пользователи")
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(description = """
            Получение информации о пользователе по его идентификатору.
            """)
    @GetMapping(USER_ID)
    public UserDto get(@PathVariable String id) {
        return userService.getById(id);
    }

    @Operation(description = """
            Регистрация нового пользователя. Сессия при регистрации не создаётся.
            """)
    @PostMapping(USERS)
    public UserDto create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }

    @Operation(description = """
            Обновление информации о пользователе.
            """)
    @PatchMapping(USERS)
    public UserDto update(@RequestBody UserPatchRequest userPatchRequest) {
        return userService.update(userPatchRequest);
    }
}
