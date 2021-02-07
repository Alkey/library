package com.library.controller;

import com.library.dto.BookDto;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController extends ControllerExceptionHandler {
    private final BookService service;

    @PostMapping
    public BookDto create(@RequestBody BookDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping
    public BookDto update(@RequestBody BookDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public BookDto delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
