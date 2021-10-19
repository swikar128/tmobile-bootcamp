package com.galvanize.tmo.paspringstarter.controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.tmo.paspringstarter.model.Book;
import com.galvanize.tmo.paspringstarter.repository.BookRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LibraryController.class})
@ExtendWith(SpringExtension.class)
class LibraryControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private LibraryController libraryController;

    @Test
    void testConstructor() {
        LibraryController actualLibraryController = new LibraryController();
        actualLibraryController.health();
        assertNull(actualLibraryController.bookRepository);
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = new Book();
        book.setId(123L);
        book.setAuthor("JaneDoe");
        book.setTitle("Dr");
        book.setYearPublished("Year Published");
        when(this.bookRepository.save((Book) any())).thenReturn(book);

        Book book1 = new Book();
        book1.setId(123L);
        book1.setAuthor("JaneDoe");
        book1.setTitle("Dr");
        book1.setYearPublished("Year Published");
        String content = (new ObjectMapper()).writeValueAsString(book1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"author\":\"JaneDoe\",\"title\":\"Dr\",\"yearPublished\":\"Year Published\"}"));
    }

    @Test
    void testDeleteAllBooks() throws Exception {
        doNothing().when(this.bookRepository).deleteAll();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/books");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteAllBooks2() throws Exception {
        doNothing().when(this.bookRepository).deleteAll();
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/books");
        deleteResult.contentType("Not all who wander are lost");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetAllBooks() throws Exception {
        when(this.bookRepository.findAll((org.springframework.data.domain.Sort) any())).thenReturn(new ArrayList<Book>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetAllBooks2() throws Exception {
        Book book = new Book();
        book.setId(123L);
        book.setAuthor("JaneDoe");
        book.setTitle("Dr");
        book.setYearPublished("?");

        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(book);
        when(this.bookRepository.findAll((org.springframework.data.domain.Sort) any())).thenReturn(bookList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books");
        MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":123,\"author\":\"JaneDoe\",\"title\":\"Dr\",\"yearPublished\":\"?\"}]"));
    }

    @Test
    void testGetAllBooks3() throws Exception {
        when(this.bookRepository.findByTitleContaining((String) any())).thenReturn(new ArrayList<Book>());
        when(this.bookRepository.findAll((org.springframework.data.domain.Sort) any())).thenReturn(new ArrayList<Book>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books").param("title", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

