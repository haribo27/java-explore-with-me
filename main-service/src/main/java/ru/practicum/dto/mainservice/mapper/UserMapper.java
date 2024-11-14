package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.mainservice.dto.comment.UserCommentDto;
import ru.practicum.dto.mainservice.dto.user.UserDto;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;
import ru.practicum.dto.mainservice.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(UserRequestDto userRequestDto);

    UserDto mapToUserDto(User user);
}
