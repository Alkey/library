package com.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<BookDto> books;
}
