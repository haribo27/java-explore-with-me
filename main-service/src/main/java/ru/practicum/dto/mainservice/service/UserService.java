package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.user.UserDto;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserRequestDto userRequestDto);


    List<UserDto> findUserByParams(List<Long> ids, Integer from, Integer size);

    void deleteUser(long userId);
}
