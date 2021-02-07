package com.library.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    private Set<Book> books;
    private boolean isDeleted;
}
