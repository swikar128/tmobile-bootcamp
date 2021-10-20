package com.galvanize.tmo.paspringstarter.controller;

import com.galvanize.tmo.paspringstarter.model.Book;
import com.galvanize.tmo.paspringstarter.model.BooksContainer;
import com.galvanize.tmo.paspringstarter.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryController {

    @GetMapping("/health")
    public void health() {
    }

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/api/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book book_res = bookRepository.save(book);
            return new ResponseEntity<>(book_res, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/api/books", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BooksContainer> getAllBooks() {
        try {
            List<Book> books = new ArrayList<>();

            bookRepository.findAll(Sort.by("title")).forEach(books::add);

            BooksContainer booksContainer = new BooksContainer(books);
            return new ResponseEntity<>(booksContainer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/books")
    public ResponseEntity<HttpStatus> deleteAllBooks() {
        try {
            bookRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}