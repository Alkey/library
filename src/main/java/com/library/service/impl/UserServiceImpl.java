package com.library.service.impl;

import com.library.dto.UserDto;
import com.library.exception.DataProcessingException;
import com.library.model.Book;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Type USER_DTO_LIST_TYPE = new TypeToken<List<UserDto>>() {}.getType();

    private final BookRepository bookDao;
    private final UserRepository userDao;
    private final ModelMapper mapper;

    @Override
    public UserDto create(String name) {
        User user = new User();
        user.setName(name);
        return mapper.map(userDao.save(user), UserDto.class);
    }

    @Override
    public UserDto get(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new DataProcessingException("User not found by id: " + id));
        if (user.isDeleted()) {
            throw new DataProcessingException("This user was removed " + id);
        }
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto dto) {
        return userDao.findById(dto.getId())
                .filter(user -> !user.isDeleted())
                .map(user -> {
                    user.setName(dto.getName());
                    return mapper.map(userDao.save(user), UserDto.class);
                })
                .orElseThrow(() -> new DataProcessingException("Can't update user with id: " + dto.getId()));
    }

    @Override
    public UserDto delete(Long id) {
        return userDao.findById(id)
                .map(user -> {
                    user.setDeleted(true);
                    return mapper.map(userDao.save(user), UserDto.class);
                })
                .orElseThrow(() -> new DataProcessingException("Can't delete this user " + id));
    }

    @Override
    @Transactional
    public UserDto addBook(Long userId, Long bookId) {
        return userDao.findById(userId)
                .filter(user -> !user.isDeleted())
                .map(user -> {
                    Book book = getBookIfFree(bookId);
                    user.getBooks().add(book);
                    return mapper.map(userDao.save(user), UserDto.class);
                })
                .orElseThrow(() -> new DataProcessingException("Can't add book with id: " + bookId + " to user with id: " + userId));
    }

    @Override
    @Transactional
    public UserDto returnBook(Long userId, Long bookId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new DataProcessingException("User not found with id: " + userId));
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new DataProcessingException("Book not found with id: " + bookId));
        user.getBooks().remove(book);
        return mapper.map(userDao.save(user), UserDto.class);
    }

    @Override
    @Transactional
    public List<UserDto> getAll() {
        return mapper.map(userDao.findAllByIsDeletedFalse(), USER_DTO_LIST_TYPE);
    }


    private Book getBookIfFree(Long bookId) {
        if (isNotFree(bookId)) {
            throw new DataProcessingException("Another user took this book already");
        }
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new DataProcessingException("This book not found: " + bookId));
        if (book.isDeleted()) {
            throw new DataProcessingException("This book was removed: " + bookId);
        }
        return book;
    }

    private boolean isNotFree(Long bookId) {
        return Optional.ofNullable(bookDao.findTakenBook(bookId))
                .orElse(false);
    }
}
