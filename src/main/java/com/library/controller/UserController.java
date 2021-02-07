package com.library.controller;

import com.library.dto.UserDto;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends ControllerExceptionHandler {
    private final UserService service;

    @PostMapping
    public UserDto create(@RequestParam String name) {
        return service.create(name);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PostMapping("/books")
    public UserDto takeBook(@RequestParam Long userId,
                            @RequestParam Long bookId) {
        return service.addBook(userId, bookId);
    }

    @PostMapping("/return")
    public UserDto returnBook(@RequestParam Long userId,
                              @RequestParam Long bookId) {
        return service.returnBook(userId, bookId);
    }

    @GetMapping("/all")
    public List<UserDto> getAll() {
        return service.getAll();
    }
}

