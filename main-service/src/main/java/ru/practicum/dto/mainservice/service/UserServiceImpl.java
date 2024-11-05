package ru.practicum.dto.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.user.UserDto;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.mapper.UserMapper;
import ru.practicum.dto.mainservice.model.User;
import ru.practicum.dto.mainservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto saveUser(UserRequestDto userRequestDto) {
        log.info("Saving user {}", userRequestDto);
        Optional<User> isUserWithEmailExists = userRepository.findByEmail(userRequestDto.getEmail());
        if (isUserWithEmailExists.isPresent()) {
            throw new ConditionsAreNotMet("User with this email already exists");
        }
        User user = userMapper.mapToUser(userRequestDto);
        user = userRepository.save(user);
        log.info("User saved success {}", user);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> findUserByParams(List<Long> ids,
                                          Integer from, Integer size) {
        log.info("Finding users by params: ids: {}, from: {}, size: {}", ids, from, size);
        List<User> queryByParamsResult;
        if (ids != null && !ids.isEmpty()) {
            log.info("If ids is null, get users by other params");
            queryByParamsResult = userRepository.findUserWithIds(ids, from, size);
        } else {
            queryByParamsResult = userRepository.findUserBy(from, size);
        }
        return queryByParamsResult.stream()
                .map(userMapper::mapToUserDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.info("Deleting user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
        userRepository.delete(user);
        log.info("User deleted success with id: {}", user);
    }
}
