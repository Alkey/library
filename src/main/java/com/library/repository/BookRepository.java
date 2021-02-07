package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    @Query(value = "select true from dual where exists(select * from users_books where books_id = :bookId)",
            nativeQuery = true)
    Boolean findTakenBook(@Param("bookId") Long bookId);
}
