package com.library.service.impl;

import com.library.dto.BookDto;
import com.library.exception.DataProcessingException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository dao;
    private final ModelMapper mapper;

    @Override
    public BookDto create(BookDto dto) {
        Optional<Book> optionalBook = dao.findByTitle(dto.getTitle());
        if (optionalBook.isEmpty()) {
            Book book = mapper.map(dto, Book.class);
            book.setDeleted(false);
            return mapper.map(dao.save(book), BookDto.class);
        }
        throw new DataProcessingException("This book is already exist");
    }

    @Override
    public BookDto read(Long id) {
        Book book = dao.findById(id)
                .orElseThrow(() -> new DataProcessingException("Book not found by id: " + id));
        if (book.isDeleted()) {
            throw new DataProcessingException("This book was removed");
        }
        return mapper.map(book, BookDto.class);
    }

    @Override
    public BookDto update(BookDto dto) {
        return dao.findById(dto.getId())
                .filter(book -> !book.isDeleted())
                .map(book -> {
                    book.setAuthor(dto.getAuthor());
                    book.setTitle(dto.getTitle());
                    return mapper.map(dao.save(book), BookDto.class);
                })
                .orElseThrow(() -> new DataProcessingException("Can't update this book " + dto));
    }

    @Override
    public BookDto delete(Long id) {
        return dao.findById(id)
                .map(book -> {
                    book.setDeleted(true);
                    return mapper.map(dao.save(book), BookDto.class);
                })
                .orElseThrow(() -> new DataProcessingException("Can't delete book with id: " + id));
    }
}
