package com.library.service;

import com.library.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(String name);

    UserDto get(Long id);

    UserDto update(UserDto dto);

    UserDto delete(Long id);

    UserDto addBook(Long userId, Long bookId);

    UserDto returnBook(Long userId, Long bookId);

    List<UserDto> getAll();
}
