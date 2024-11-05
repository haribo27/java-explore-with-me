package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.user.UserRequestDto;
import ru.practicum.dto.mainservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.saveUser(userRequestDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam(value = "ids", required = false) List<Long> ids,
                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(userService.findUserByParams(ids, from, size),
                HttpStatus.OK);
    }
}
