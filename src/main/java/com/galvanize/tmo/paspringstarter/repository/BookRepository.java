package com.galvanize.tmo.paspringstarter.repository;

import com.galvanize.tmo.paspringstarter.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {

}
