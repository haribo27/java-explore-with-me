package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.user.UserDto;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDto saveUser(UserRequestDto userRequestDto);

    List<UserDto> findUserByParams(Set<Long> ids, Integer from, Integer size);

    void deleteUser(long userId);
}
