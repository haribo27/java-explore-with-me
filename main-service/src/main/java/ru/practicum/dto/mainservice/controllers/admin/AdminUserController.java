package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.user.UserDto;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;
import ru.practicum.dto.mainservice.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.saveUser(userRequestDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUser(@RequestParam(value = "ids", required = false) Set<Long> ids,
                                                 @RequestParam(value = "from", defaultValue = "0")
                                                 @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10")
                                                 @Positive Integer size) {
        return ResponseEntity.ok(userService.findUserByParams(ids, from, size));
    }
}
