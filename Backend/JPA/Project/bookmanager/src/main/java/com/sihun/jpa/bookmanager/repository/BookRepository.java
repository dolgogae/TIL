package com.sihun.jpa.bookmanager.repository;

import com.sihun.jpa.bookmanager.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
