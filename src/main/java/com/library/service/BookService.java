package com.library.service;

import com.library.dto.BookDto;

public interface BookService {
    BookDto create(BookDto dto);

    BookDto read(Long id);

    BookDto update(BookDto dto);

    BookDto delete(Long id);
}
